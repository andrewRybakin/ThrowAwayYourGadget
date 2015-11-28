package ru.dzen.besraznitsy;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ru.dzen.besraznitsy.bluetooth.BluetoothController;

public class SeekGamesFragment extends ListFragment {

    public static final String DEVICE_ACTIVATED = "DeviceActivated";
    public static final String DEVICE_DEACTIVATED = "DeviceDeActivated";
    public static final String GAME_STOPPED = "gameStopped";
    public static final String GAME_STARTED = "gameStarted";

    private ArrayAdapter adapter;
    private BluetoothController.EventsReceiver mReceiver;
    private TextView headerView;
    private LinearLayout footerView;

    public SeekGamesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothController.getInstance(getActivity());
        getActivity().registerReceiver(mReceiver = new BluetoothController.EventsReceiver() {
            @Override
            public void onNewServerFound(String serverName) {
                adapter.add(serverName);
            }

            @Override
            public void onDiscoveryFinish() {
                //Показать что завершено сканирование и предложить новое
                Log.d("Блютуз", "StopSellingShitFromBoxes");
                headerView.setText("Founded servers");
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
        adapter = new ArrayAdapter<>(SeekGamesFragment.this.getActivity(), R.layout.seek_games_fragment_listitem, R.id.text);
        adapter.setNotifyOnChange(true);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seek_games_fragment, container, false);
        headerView = (TextView) inflater.inflate(R.layout.seek_games_fragment_list_headfoot, null);
        //footerView = (LinearLayout)inflater.inflate(R.layout.seek_games_fragment_list_headfoot, null);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headerView.setText(getString(R.string.searching_header));
        getListView().addHeaderView(headerView, null, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
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
