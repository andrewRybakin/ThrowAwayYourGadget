package ru.dzen.besraznitsy.supervisor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class SupervisorService extends Service implements SensorEventListener {

    private static final String LOG_TAG = "SupervisorSensor";
    private boolean gameStarted;
    private BroadcastReceiver mReceiver;

    private boolean proximityClose;
    private boolean screenDown;
    private boolean screenOn;

    private boolean lastState;

    public SupervisorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "OnCreate()");
        SensorManager sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        gameStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SupervisorBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(isGameStarted())stopGame();
        if(intent.getType().equals(Intent.ACTION_APP_ERROR))
            stopSelf();
        return super.onUnbind(intent);
    }

    public void startGame() {
        gameStarted = true;
        lastState=isDeviceDeactivated();
        SupervisorController.getInstance().onGameStart(System.currentTimeMillis(), getApplicationContext());
        PowerManager pm=(PowerManager)getSystemService(POWER_SERVICE);
        screenOn=pm.isScreenOn();
    }

    public void stopGame(){
        gameStarted=false;
        SupervisorController.getInstance().onGameEnd(System.currentTimeMillis(), getApplicationContext());
    }

    public boolean isGameStarted(){
        return gameStarted;
    }

    public class SupervisorBinder extends Binder {
        private SupervisorService service;

        public SupervisorBinder(SupervisorService supervisor) {
            service = supervisor;
        }

        public SupervisorService getService() {
            return service;
        }
    }

    private class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                screenOn = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                screenOn = true;
            }
            if(gameStarted)
                if(isDeviceDeactivated()!= lastState){
                    lastState=isDeviceDeactivated();
                    sendInfo();
                }
        }
    }

    private void sendInfo() {
        if(!lastState){
            SupervisorController.getInstance().onDeviceActivated(System.currentTimeMillis(), getApplicationContext());
        }else
            SupervisorController.getInstance().onDeviceDeActivated(System.currentTimeMillis(), getApplicationContext());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityClose = event.values[0] == 0;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] g = event.values.clone();
            double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
            g[2] = (float) (g[2] / norm_Of_g);
            screenDown = g[2] < -0.85;
        }
        if(gameStarted)
            if(isDeviceDeactivated()!= lastState){
                lastState=isDeviceDeactivated();
                sendInfo();
            }
    }

    private boolean isDeviceDeactivated(){
        return (screenDown&&proximityClose)||(screenDown&&!screenOn)||(proximityClose&&!screenOn);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

