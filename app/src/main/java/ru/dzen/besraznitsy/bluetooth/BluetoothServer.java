package ru.dzen.besraznitsy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothServer implements BluetoothInterface {
    ArrayList<BluetoothSocket> socketPool = new ArrayList<>();
    Thread listener;
    boolean start = false;
    String name;
    BluetoothAdapter mBAdapter;
    Context mContext;
    int maxClients;

    public BluetoothServer(BluetoothAdapter mBAdapter, Context mContext, int maxClients) {
        this.mBAdapter = mBAdapter;
        this.mContext = mContext;
        this.maxClients = maxClients;
        name = mBAdapter.getName();
        start = true;
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(discoverableIntent);

        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startServer() throws IOException {

        mBAdapter.setName(BluetoothInterface.PREFIX + name);
        final BluetoothServerSocket mmServerSocket =
                mBAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString(MY_UUID));

        listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    try {
                        socketPool.add(mmServerSocket.accept());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        listener.start();
        ServerWorker();
    }

    public void stopServer() {
        start = false;
        mBAdapter.setName(name);
    }

    private void ServerWorker() throws IOException {
        while (true) {
            for (BluetoothSocket s : socketPool) {
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String str = "";
                while ((str = br.readLine()) != null) {
                    for (BluetoothSocket s1 : socketPool) {
                        if (s1 != s) {
                            PrintWriter pw = new PrintWriter(s1.getOutputStream());
                            pw.write(str);
                            pw.close();
                        }
                    }
                }

            }
        }
    }
}
