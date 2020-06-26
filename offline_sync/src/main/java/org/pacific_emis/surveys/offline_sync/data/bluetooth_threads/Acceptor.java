package org.pacific_emis.surveys.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import org.pacific_emis.surveys.offline_sync.R;
import org.pacific_emis.surveys.offline_sync.data.exceptions.BluetoothGenericException;
import io.reactivex.schedulers.Schedulers;

public class Acceptor {

    private static final String TAG = Acceptor.class.getName();

    private final BluetoothServerSocket serverSocket;
    private final OnSocketAcceptedListener socketAcceptedListener;

    public Acceptor(Context context, BluetoothAdapter adapter, OnSocketAcceptedListener socketAcceptedListener) {
        this.socketAcceptedListener = socketAcceptedListener;
        BluetoothServerSocket tempSocket = null;

        try {
            tempSocket = adapter.listenUsingRfcommWithServiceRecord(
                    context.getString(R.string.app_name),
                    UUID.fromString(context.getString(R.string.rfcomm_uuid))
            );
        } catch (IOException ex) {
            Log.e(TAG, "Socket's listen() method failed", ex);
            throw new BluetoothGenericException(ex);
        }

        serverSocket = tempSocket;
    }

    public void start() {
        Schedulers.newThread().scheduleDirect(() -> {
            BluetoothSocket socket;

            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException ex) {
                    Log.e(TAG, "Socket's accept() method failed", ex);
                    break;
                }

                if (socket != null) {
                    socketAcceptedListener.onSocketAccept(socket);
                    end();
                    break;
                }
            }
        });
    }

    public void end() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
            throw new BluetoothGenericException(e);
        }
    }

    public interface OnSocketAcceptedListener {
        void onSocketAccept(BluetoothSocket socket);
    }
}
