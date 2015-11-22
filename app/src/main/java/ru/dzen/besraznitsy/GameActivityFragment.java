package ru.dzen.besraznitsy;

import android.app.Activity;
import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ru.dzen.besraznitsy.bluetooth.BluetoothController;

public class GameActivityFragment extends ListFragment {

    public static final String DEVICE_ACTIVATED = "DeviceActivated";
    public static final String DEVICE_DEACTIVATED = "DeviceDeActivated";
    public static final String EXTRA_TIME = "time";
    public static final String GAME_STOPPED = "gameStopped";
    public static final String GAME_STARTED = "gameStarted";
    public static final String EXTRA_TOTALSCORE = "extraTotalScore";
    public static final String EXTRA_SCORE = "extraScore";

    private ArrayAdapter adapter;

    public GameActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothController.getInstance(null);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mReceiver, filter);
        adapter = new ArrayAdapter<>(GameActivityFragment.this.getActivity(), R.layout.uni_list_item, R.id.text);
        adapter.setNotifyOnChange(true);
        setListAdapter(adapter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("Блютуз", "Чет пришло на ресивер");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("Блютуз", device.getName());
                if(device.getName().contains(BluetoothController.PREFIX))
                    adapter.add(device.getName().substring(BluetoothController.PREFIX.length()));
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("Блютуз", "EndSearch");
            }
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                Log.d("Блютуз", intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0) + "="+ BluetoothAdapter.STATE_ON);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_fragment, container, false);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}
