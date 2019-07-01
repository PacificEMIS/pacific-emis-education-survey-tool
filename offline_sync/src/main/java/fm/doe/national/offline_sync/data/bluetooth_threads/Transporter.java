package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import io.reactivex.schedulers.Schedulers;

public class Transporter {

    private static final String TAG = Transporter.class.getName();
    private static final String MARK_END = "MARK_END";
    private static final int SIZE_MEMORY_BUFFER = 1024;

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
            Runnable disconnectRunnable = listener::onConnectionLost;
            ByteBuffer messageBuffer = null;
            int bytesToRead;
            int readBytesCount;

            while (connectionState == ConnectionState.CONNECTED) {
                if (!bluetoothSocket.isConnected()) {
                    disconnectRunnable.run();
                    break;
                }

                try {
                    while ((bytesToRead = inputStream.available()) > 0) {
                        byte[] buffer = new byte[SIZE_MEMORY_BUFFER];

                        if (messageBuffer == null) {
                            messageBuffer = ByteBuffer.allocate(bytesToRead);
                        }

                        if (bytesToRead > SIZE_MEMORY_BUFFER) {
                            readBytesCount = inputStream.read(buffer, 0, SIZE_MEMORY_BUFFER);
                        } else {
                            readBytesCount = inputStream.read(buffer, 0, bytesToRead);
                        }

                        int capacityDelta = readBytesCount - messageBuffer.remaining();

                        if (capacityDelta > 0) {
                            ByteBuffer extendedBuffer = ByteBuffer.allocate(messageBuffer.capacity() + capacityDelta);
                            extendedBuffer.put(messageBuffer.array(), 0, messageBuffer.position());
                            messageBuffer = extendedBuffer;
                        }

                        // TODO: send receiving count event
                        Log.d(TAG, "<=== bytes count = " + readBytesCount);

                        messageBuffer.put(buffer, 0, readBytesCount);
                    }

                    if (messageBuffer != null) {
                        String chunk = new String(messageBuffer.array());

                        if (chunk.contains(MARK_END)) {
                            chunk = chunk.replace(MARK_END, "");
                            Log.d(TAG, "<=== " + chunk);
                            listener.onMessageObtain(chunk);
                            messageBuffer = null;
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "<===\n", e);
                    disconnectRunnable.run();
                }
            }
        });
    }

    public void write(String message) {
        Schedulers.newThread().scheduleDirect(() -> {
            try {
                String formattedMessage = message + MARK_END;
                Log.d(TAG, "===>\n" + formattedMessage);
                // TODO: send receiving count event
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
