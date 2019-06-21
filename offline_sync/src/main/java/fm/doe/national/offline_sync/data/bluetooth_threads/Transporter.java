package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.schedulers.Schedulers;

public class Transporter {

    private static final String TAG = Transporter.class.getName();

    private final Listener listener;
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private ConnectionState connectionState;

    public Transporter(BluetoothSocket socket, Listener listener) {
        this.bluetoothSocket = socket;
        this.listener = listener;
        InputStream tempInputStream = null;
        OutputStream tempOutputStream = null;

        try {
            tempInputStream = socket.getInputStream();
            tempOutputStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        inputStream = tempInputStream;
        outputStream = tempOutputStream;
    }

    public void start() {
        Schedulers.newThread().scheduleDirect(() -> {
            byte[] buffer = new byte[1024];
            int bytes;

            while (connectionState == ConnectionState.CONNECTED) {
                try {
                    bytes = inputStream.read(buffer);
                    listener.onMessageObtain(buffer, bytes);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    listener.onConnectionLost();
                    break;
                }
            }
        });
    }

    public void write(byte[] buffer) {
        Schedulers.newThread().scheduleDirect(() -> {
            try {
                outputStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        });
    }

    public void end() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public synchronized void setState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public interface Listener {
        void onMessageObtain(byte[] buffer, int byteCount);
        void onConnectionLost();
    }
}
