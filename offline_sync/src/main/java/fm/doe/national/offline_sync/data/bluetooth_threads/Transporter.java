package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
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
            Log.e(TAG, "temp streams not created", e);
        }

        inputStream = tempInputStream;
        outputStream = tempOutputStream;
    }

    public void start() {
        Schedulers.newThread().scheduleDirect(() -> {
            while (connectionState == ConnectionState.CONNECTED) {
                try {
                    if (inputStream.available() > 0) {
                        SystemClock.sleep(100); // wait for all data
                        int bytesToRead = inputStream.available();
                        byte[] buffer = new byte[bytesToRead];
                        inputStream.read(buffer, 0, bytesToRead);
                        listener.onMessageObtain(new String(buffer));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    if (!bluetoothSocket.isConnected()) {
                        listener.onConnectionLost();
                        break;
                    }
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
            inputStream.close();
            outputStream.close();
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public synchronized void setState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public interface Listener {
        void onMessageObtain(String message);

        void onConnectionLost();
    }
}
