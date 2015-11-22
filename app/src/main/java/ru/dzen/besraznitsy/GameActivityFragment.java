package ru.dzen.besraznitsy;

import android.app.Fragment;
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
import android.widget.TextView;

import java.util.ArrayList;

import ru.dzen.besraznitsy.bluetooth.BluetoothController;
import ru.dzen.besraznitsy.bluetooth.Constants;

public class GameActivityFragment extends ListFragment {

    public static final String DEVICE_ACTIVATED = "DeviceActivated";
    public static final String DEVICE_DEACTIVATED = "DeviceDeActivated";
    public static final String EXTRA_TIME = "time";
    public static final String GAME_STOPPED = "gameStopped";
    public static final String GAME_STARTED = "gameStarted";
    public static final String EXTRA_TOTALSCORE = "extraTotalScore";
    public static final String EXTRA_SCORE = "extraScore";

    private TextView tw;
    private MyArrayAdapter adapter;
    private ArrayList<BluetoothDevice> servers;

    public GameActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothController.getInstance(getActivity());
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
        servers=new ArrayList<>();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                Log.d("Блютузепта", device.getName());
                servers.add(device);
                //}
                // When discovery is finished, change the Activity title
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                adapter = new MyArrayAdapter(GameActivityFragment.this.getActivity(), R.layout.uni_list_item);
                Log.d("Блютузепта", "EndSearch");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_fragment, container, false);

        IntentFilter iFilter = new IntentFilter(DEVICE_ACTIVATED);
        iFilter.addAction(DEVICE_DEACTIVATED);
        iFilter.addAction(GAME_STARTED);
        iFilter.addAction(GAME_STOPPED);
        iFilter.addAction(GameActivity.REQUEST_BLUETOOTH);
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
                if(checkType(intent, GameActivity.REQUEST_BLUETOOTH)) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                    Log.d("Блютузепта", "bluetooth enable intent");
                }
                if (checkType(intent, DEVICE_ACTIVATED)) {
                    //tw.setText("Your score: "+intent.getFloatExtra(EXTRA_SCORE,0));
                    //Log.d("ПриветКарл", "DeviceActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
                if (checkType(intent, DEVICE_DEACTIVATED)) {
                    //tw.setText("Your score: "+intent.getFloatExtra(EXTRA_SCORE,0));
                    //Log.d("ПриветКарл", "DeviceDeActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
                if (checkType(intent, GAME_STARTED)) {
                    //tw.setText("Your score: "+intent.getFloatExtra(EXTRA_SCORE,0));
                    //Log.d("ПриветКарл", "DeviceDeActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
                if (checkType(intent, GAME_STOPPED)) {
                    //tw.setText("Total score: "+intent.getFloatExtra(EXTRA_TOTALSCORE,0));
                    //Log.d("ПриветКарл", "DeviceDeActivated on:"+intent.getLongExtra(EXTRA_TIME, 0));
                }
            }
        }, iFilter);
        //tw=(TextView)v.findViewById(R.id.score);
        return v;
    }

    private class MyArrayAdapter extends ArrayAdapter<BluetoothDevice> {

        public MyArrayAdapter(Context c, int resId) {
            super(c, resId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BluetoothDevice item = servers.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.uni_list_item, null);
            }
            ((TextView) convertView.findViewById(R.id.text_view)).setText(item.getName()/*.substring(Constants.prefix.length())*/);
            return convertView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}
