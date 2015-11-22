package ru.dzen.besraznitsy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ru.dzen.besraznitsy.GameActivity;
import ru.dzen.besraznitsy.GameActivityFragment;

public class BluetoothController {

    public static final String PREFIX = "ru.dzen/";
    private static Context mContext;
    private static BluetoothController ourInstance;
    private BluetoothAdapter mBAdapter;

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
            } else {
                startDiscovering();
            }
        }
    }

    public void startDiscovering() {
        if (mBAdapter.isDiscovering()) mBAdapter.cancelDiscovery();
        mBAdapter.startDiscovery();
        Log.d("Блютуз", "StartSellingShitInBoxes");
    }
}
