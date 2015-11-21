package ru.dzen.besraznitsy;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class BluetoothController {

    private static final String prefix="ru.dzen/";
    private static Context mContext;
    private static BluetoothController ourInstance = new BluetoothController();
    private BluetoothAdapter mBAdapter;

    public static BluetoothController getInstance(Context c) {
        mContext=c;
        return ourInstance;
    }

    private BluetoothController() {
        mBAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBAdapter.isEnabled()){
            Intent i=new Intent();
            i.setAction(MainActivity.REQUEST_BLUETOOTH);
            i.setType("text/*");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
        }
    }
}
