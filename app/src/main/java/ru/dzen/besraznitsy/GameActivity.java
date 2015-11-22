package ru.dzen.besraznitsy;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.dzen.besraznitsy.supervisor.SupervisorService;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "UserName";
    private ServiceConnection aPConnection;
    private SupervisorService mService;
    private Intent serviceIntent;
    private FloatingActionButton fab;
    public static final String REQUEST_BLUETOOTH="BluetoothRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mService.isGameStarted()) {
                    mService.startGame();
                    Snackbar.make(view, "StartGame", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    mService.stopGame();
                    Snackbar.make(view, "StopGame", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        GameActivityFragment gameActivityFragment =new GameActivityFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, gameActivityFragment).commit();

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }, IntentFilter.create(REQUEST_BLUETOOTH, "text/*"));
        aPConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((SupervisorService.SupervisorBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        serviceIntent = new Intent(this, SupervisorService.class);
        if(savedInstanceState==null)startService(serviceIntent);
        bindService(serviceIntent, aPConnection, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(aPConnection);
    }
}
