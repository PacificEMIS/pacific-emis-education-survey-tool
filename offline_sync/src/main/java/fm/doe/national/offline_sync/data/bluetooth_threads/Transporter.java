package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.schedulers.Schedulers;

public class Transporter {

    private static final String TAG = Transporter.class.getName();
    private static final String MARK_END = "MSG_END";

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
            StringBuilder messageBuilder = new StringBuilder();
            while (connectionState == ConnectionState.CONNECTED) {
                try {
                    int bytesToRead = inputStream.available();
                    if (bytesToRead > 0) {
                        byte[] buffer = new byte[bytesToRead];
                        inputStream.read(buffer, 0, bytesToRead);
                        String readedString = new String(buffer);
                        Log.d(TAG, "<===\n" + readedString + "\n<===");
                        messageBuilder.append(readedString);
                    } else if (messageBuilder.length() > 0) {
                        String message = messageBuilder.toString();
                        if (message.contains(MARK_END)) {
                            message = message.replace(MARK_END, "");
                            listener.onMessageObtain(message);
                            messageBuilder = new StringBuilder();
                        }
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

    public void write(String message) {
        Schedulers.newThread().scheduleDirect(() -> {
            try {
                String formattedMessage = message + MARK_END;
                Log.d(TAG, "===>\n" + formattedMessage + "\n===>");
                outputStream.write(formattedMessage.getBytes());
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
