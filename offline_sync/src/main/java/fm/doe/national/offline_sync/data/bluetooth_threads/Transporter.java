package fm.doe.national.offline_sync.data.bluetooth_threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import fm.doe.national.offline_sync.data.exceptions.BluetoothGenericException;
import io.reactivex.schedulers.Schedulers;

public class Transporter {

    private static final String TAG = Transporter.class.getName();
    private static final int SIZE_MEMORY_BUFFER = 8192;
    private static final byte[] TERMINATING_BYTES = "MSG_END".getBytes();

    private final Listener listener;
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private ConnectionState connectionState;

    public Transporter(BluetoothSocket socket, Listener listener) {
        this.bluetoothSocket = socket;
        this.listener = listener;
        InputStream tempInputStream;
        OutputStream tempOutputStream;

        try {
            tempInputStream = socket.getInputStream();
            tempOutputStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp streams not created", e);
            throw new BluetoothGenericException(e);
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
            byte[] buffer = new byte[SIZE_MEMORY_BUFFER];

            while (connectionState == ConnectionState.CONNECTED) {
                if (!bluetoothSocket.isConnected()) {
                    disconnectRunnable.run();
                    break;
                }

                try {
                    while ((bytesToRead = inputStream.available()) > 0) {
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

                        Log.d(TAG, "<=== bytes count = " + readBytesCount);

                        messageBuffer.put(buffer, 0, readBytesCount);
                    }

                    if (messageBuffer != null) {
                        byte[] bytes = messageBuffer.array();

                        if (bytes.length > TERMINATING_BYTES.length &&
                                Arrays.equals(
                                        TERMINATING_BYTES,
                                        Arrays.copyOfRange(
                                                bytes,
                                                bytes.length - TERMINATING_BYTES.length,
                                                bytes.length
                                        )
                                )
                        ) {
                            listener.onMessageObtain(Arrays.copyOfRange(bytes, 0, bytes.length - TERMINATING_BYTES.length));
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

    public void write(byte[] bytes) {
        Schedulers.newThread().scheduleDirect(() -> {
            try {
                outputStream.write(bytes);
                outputStream.write(TERMINATING_BYTES);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
                listener.onConnectionLost();
            }
        });
    }

    public void write(String message) {
        write(message.getBytes());
    }

    public void end() {
        try {
            inputStream.close();
            outputStream.close();
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
            throw new BluetoothGenericException(e);
        }
    }

    public synchronized void setState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public BluetoothDevice getDevice() {
        return bluetoothSocket.getRemoteDevice();
    }

    public interface Listener {
        void onMessageObtain(byte[] message);

        void onConnectionLost();
    }
}
