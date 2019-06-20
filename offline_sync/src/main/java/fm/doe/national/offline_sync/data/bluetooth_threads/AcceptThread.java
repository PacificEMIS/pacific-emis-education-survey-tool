package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import fm.doe.national.offline_sync.R;

public class AcceptThread extends Thread {

    private static final String TAG = AcceptThread.class.getName();

    private final BluetoothServerSocket serverSocket;
    private final OnSocketAcceptedListener socketAcceptedListener;

    public AcceptThread(Context context, BluetoothAdapter adapter, OnSocketAcceptedListener socketAcceptedListener) {
        this.socketAcceptedListener = socketAcceptedListener;
        BluetoothServerSocket tempSocket = null;

        try {
            tempSocket = adapter.listenUsingRfcommWithServiceRecord(
                    context.getString(R.string.app_name),
                    UUID.fromString(context.getString(R.string.rfcomm_uuid))
            );
        } catch (IOException ex) {
            Log.e(TAG, "Socket's listen() method failed", ex);
        }

        serverSocket = tempSocket;
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                Log.e(TAG, "Socket's accept() method failed", ex);
                break;
            }

            if (socket != null) {
                socketAcceptedListener.onSocketAccept(socket);
                cancel();
                break;
            }
        }
    }

    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

    public interface OnSocketAcceptedListener {
        void onSocketAccept(BluetoothSocket socket);
    }
}
