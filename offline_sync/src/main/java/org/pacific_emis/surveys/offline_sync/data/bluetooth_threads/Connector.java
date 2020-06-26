package org.pacific_emis.surveys.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import org.pacific_emis.surveys.offline_sync.R;
import org.pacific_emis.surveys.offline_sync.data.exceptions.BluetoothGenericException;
import io.reactivex.Single;

public class Connector {

    private static final String TAG = Connector.class.getName();

    private final BluetoothSocket bluetoothSocket;
    private final BluetoothAdapter bluetoothAdapter;

    public Connector(Context context,
                     BluetoothAdapter adapter,
                     BluetoothDevice device) {
        this.bluetoothAdapter = adapter;
        BluetoothSocket tmp = null;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(context.getString(R.string.rfcomm_uuid)));
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
            throw new BluetoothGenericException(e);
        }

        bluetoothSocket = tmp;
    }

    public Single<BluetoothSocket> connect() {
        return Single.fromCallable(() -> {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();
            bluetoothSocket.connect();
            return bluetoothSocket;
        });
    }

    public void end() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
            throw new BluetoothGenericException(e);
        }
    }

}
