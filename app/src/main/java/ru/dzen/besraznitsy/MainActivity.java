package ru.dzen.besraznitsy;

import android.app.Fragment;
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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity  {

    private static final String LOG_TAG = "MainActivity";
    public static final String REQUEST_BLUETOOTH="BluetoothRequest";

    private ServiceConnection aPConnection;
    private SupervisorService mService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        MainActivityFragment mainActivityFragment=new MainActivityFragment();
        SplashFragment splash=new SplashFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, splash).commit();
        //getFragmentManager().beginTransaction().replace(R.id.fragment_container, mainActivityFragment).commit();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(aPConnection);
        stopService(serviceIntent);
    }
}
