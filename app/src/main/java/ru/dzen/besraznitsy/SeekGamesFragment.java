package ru.dzen.besraznitsy;

import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ru.dzen.besraznitsy.bluetooth.BluetoothController;

public class SeekGamesFragment extends ListFragment {

    public static final String DEVICE_ACTIVATED = "DeviceActivated";
    public static final String DEVICE_DEACTIVATED = "DeviceDeActivated";
    public static final String GAME_STOPPED = "gameStopped";
    public static final String GAME_STARTED = "gameStarted";

    private ArrayAdapter adapter;
    private BluetoothController.EventsReceiver mReceiver;

    public SeekGamesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothController.getInstance(null);
        getActivity().registerReceiver(mReceiver =new BluetoothController.EventsReceiver() {
            @Override
            public void onNewServerFound(String serverName) {
                adapter.add(serverName);
            }

            @Override
            public void onDiscoveryFinish() {
                //Показать что завершено сканирование и предложить новое
            }

            @Override
            public void onAdapterOn() {
                //Включение адаптера
                BluetoothController.getInstance(null).startDiscovering();
            }

            @Override
            public void onAdapterOff() {
                //Выключение адаптера
            }
        }, BluetoothController.EVENTS_RECEIVER_FILTER);
        //Перегрузить собственный адаптер для отображения в листе вьюх со статусом сервера
        adapter = new ArrayAdapter<>(SeekGamesFragment.this.getActivity(), R.layout.uni_list_item, R.id.text);
        adapter.setNotifyOnChange(true);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_fragment, container, false);
        return v;
    }

    /**
     Здесь мог бы быть наш адаптер
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}
