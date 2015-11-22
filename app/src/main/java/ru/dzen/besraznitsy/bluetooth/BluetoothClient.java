package ru.dzen.besraznitsy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothClient {

    private ArrayList<BluetoothDevice> servers;

    public void startClient(Context mContext) {

    }

    public BluetoothDevice[] getServers() {
        return (BluetoothDevice[])servers.toArray();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.MY_UUID));
            } catch (IOException e) {
                ;
            }
            mmSocket = tmp;
        }

        public void run() {
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {

                }
                return;
            }

            // управлчем соединением (в отдельном потоке)
            try {
                manageConnectedSocket(mmSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * отмена ожидания сокета
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) throws IOException {
        PrintWriter out = new PrintWriter(mmSocket.getOutputStream());
        InputStreamReader is = new InputStreamReader(mmSocket.getInputStream());
        BufferedReader in = new BufferedReader(is);
        while (true) {
            out.write(System.currentTimeMillis() + "");
            if (in.ready()) {
                Log.d("Blue", in.readLine());
            }
            try {
                Thread.currentThread().sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
