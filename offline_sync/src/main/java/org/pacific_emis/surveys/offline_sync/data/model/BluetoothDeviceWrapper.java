package org.pacific_emis.surveys.offline_sync.data.model;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

public final class BluetoothDeviceWrapper extends Device {

    @NonNull
    private final BluetoothDevice btDevice;

    public BluetoothDeviceWrapper(@NonNull BluetoothDevice device) {
        super(device.getName(), device.getAddress());
        btDevice = device;
    }

    @NonNull
    public BluetoothDevice getBtDevice() {
        return btDevice;
    }
}
