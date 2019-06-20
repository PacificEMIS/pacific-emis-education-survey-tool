package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import fm.doe.national.offline_sync.R;

public class ConnectThread extends Thread {

    private static final String TAG = ConnectThread.class.getName();

    private final BluetoothSocket bluetoothSocket;
    private final BluetoothAdapter bluetoothAdapter;
    private final OnConnectionAttemptListener connectionAttemptListener;

    public ConnectThread(Context context,
                         BluetoothAdapter adapter,
                         BluetoothDevice device,
                         OnConnectionAttemptListener connectionAttemptListener) {
        this.connectionAttemptListener = connectionAttemptListener;
        this.bluetoothAdapter = adapter;
        BluetoothSocket tmp = null;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(context.getString(R.string.rfcomm_uuid)));
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }

        bluetoothSocket = tmp;
    }

    @Override
    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();

        try {
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            cancel();
            return;
        }

        connectionAttemptListener.onConnectionAttempted(bluetoothSocket);
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

    public interface OnConnectionAttemptListener {
        void onConnectionAttempted(BluetoothSocket socket);
    }

}
