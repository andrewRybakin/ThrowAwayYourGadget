package ru.dzen.besraznitsy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {

    private final String TAG = "SplashActivity";
    private final String THREAD_STARTED = "com.mercury.first.splash.SplashActivity.isThreadStarted";
    private boolean isThreadStarted;
    private Thread splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (savedInstanceState != null) {
            isThreadStarted = savedInstanceState.getBoolean(THREAD_STARTED, false);
            Log.d(TAG, "Выпихнули: " + isThreadStarted);
        }
        if (!isThreadStarted) {
            splash = new Thread(new Runnable() {
                @Override
                public void run() {
                    isThreadStarted = true;
                    try {
                        Thread.sleep(3000);
                        Intent i = new Intent(SplashActivity.this, StartActivity.class);
                        startActivity(i);
                        finish();
                    } catch (InterruptedException e) {
                        finish();
                    }
                }
            });
            Log.d(TAG, "Запускаем");
            splash.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(THREAD_STARTED, isThreadStarted);
        Log.d(TAG, "Запихнули: " + isThreadStarted);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isThreadStarted)
            splash.interrupt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isThreadStarted)
            splash.interrupt();
    }
}
