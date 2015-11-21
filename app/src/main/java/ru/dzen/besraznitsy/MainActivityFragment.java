package ru.dzen.besraznitsy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityFragment extends Fragment {

    public static final String DEVICE_ACTIVATED = "DeviceActivated";
    public static final String DEVICE_DEACTIVATED = "DeviceDeActivated";
    public static final String EXTRA_TIME = "time";
    public static final String GAME_STOPPED = "gameStopped";
    public static final String GAME_STARTED = "gameStarted";
    private TextView tw;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        IntentFilter iFilter=new IntentFilter(DEVICE_ACTIVATED);
        iFilter.addAction(DEVICE_DEACTIVATED);
        iFilter.addAction(GAME_STARTED);
        iFilter.addAction(GAME_STOPPED);
        try {
            iFilter.addDataType("text/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            private boolean checkType(Intent i, String type) {
                return i.getAction().equals(type);
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                if (checkType(intent, DEVICE_ACTIVATED)) {
                    tw.setText(tw.getText()+"DeviceActivated on: "+intent.getLongExtra(EXTRA_TIME, 0)+"\n");
                    //Log.d("ПриветКарл", "DeviceActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
                if (checkType(intent, DEVICE_DEACTIVATED)) {
                    tw.setText(tw.getText()+"DeviceDEActivated on: "+intent.getLongExtra(EXTRA_TIME, 0)+"\n");
                    //Log.d("ПриветКарл", "DeviceDeActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
                if (checkType(intent, GAME_STARTED)) {
                    tw.setText(tw.getText()+"GameStarted on: "+intent.getLongExtra(EXTRA_TIME, 0)+"\n");
                    //Log.d("ПриветКарл", "DeviceDeActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
                if (checkType(intent, GAME_STOPPED)) {
                    tw.setText(tw.getText()+"GameStopped on: "+intent.getLongExtra(EXTRA_TIME, 0)+"\n");
                    //Log.d("ПриветКарл", "DeviceDeActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
            }
        }, iFilter);
        tw=(TextView)v.findViewById(R.id.log_text_view);
        return v;
    }
}
