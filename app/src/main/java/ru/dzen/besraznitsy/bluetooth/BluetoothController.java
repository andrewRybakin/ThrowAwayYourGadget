package ru.dzen.besraznitsy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ru.dzen.besraznitsy.GameActivity;

public class BluetoothController {

    public static final String REQUEST_BLUETOOTH="requestBluetooth";
    public static final IntentFilter ENABLE_BT_FILTER=IntentFilter.create(REQUEST_BLUETOOTH, "text/*");

    private static Context mContext;
    private static BluetoothController ourInstance;
    private BluetoothAdapter mBAdapter;

    public static final BroadcastReceiver ENABLE_BT_RECIEVER=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REQUEST_BLUETOOTH)) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enableBtIntent);
            }
        }
    };

    public static abstract class EventsReceiver extends BroadcastReceiver {
        public abstract void onNewServerFound(String serverName);
        public abstract void onDiscoveryFinish();
        public abstract void onAdapterOn();
        public abstract void onAdapterOff();

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("Блютуз", "Чет пришло на ресивер");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName().contains(BluetoothInterface.PREFIX))
                    onNewServerFound(device.getName().substring(BluetoothInterface.PREFIX.length()));
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                onDiscoveryFinish();
            }
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)==BluetoothAdapter.STATE_ON){
                    onAdapterOn();
                }else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)==BluetoothAdapter.STATE_OFF){
                    onAdapterOff();
                }
            }
        }
    };

    public static BluetoothController getInstance(Context c) {
        if (c != null)
            mContext = c;
        if (ourInstance == null)
            return ourInstance = new BluetoothController();
        else
            return ourInstance;
    }

    private BluetoothController() {
        mBAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBAdapter != null) {
            if (!mBAdapter.isEnabled()) {
                Intent i = new Intent(GameActivity.REQUEST_BLUETOOTH);
                i.setType("text/*");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
                Log.d("Блютуз", "Включаем!!!");
            }
        }
    }

    public void startDiscovering() {
        if (mBAdapter.isDiscovering()) mBAdapter.cancelDiscovery();
        mBAdapter.startDiscovery();
        Log.d("Блютуз", "StartSellingShitInBoxes");
    }
}
