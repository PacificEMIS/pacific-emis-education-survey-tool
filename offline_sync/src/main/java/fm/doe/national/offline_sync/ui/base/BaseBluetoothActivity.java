package fm.doe.national.offline_sync.ui.base;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.ui.screens.base.BaseActivity;

public abstract class BaseBluetoothActivity extends BaseActivity implements BaseBluetoothView {

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 987;
    private static final int DISCOVERABILITY_DURATION_SEC = 300;
    private static final int REQUEST_CODE_DISCOVERABILITY = 300;
    private static final String bluetoothPermission = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_DISCOVERABILITY && resultCode != RESULT_CANCELED) {
            getBaseBluetoothPresenter().onBecomeDiscoverable();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS &&
                permissions.length > 0 &&
                permissions[0].equals(bluetoothPermission) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getBaseBluetoothPresenter().onBluetoothPermissionsGranted();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void requestBluetoothPermissions() {
        if (checkSelfPermission(bluetoothPermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{bluetoothPermission}, REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            getBaseBluetoothPresenter().onBluetoothPermissionsGranted();
        }
    }

    @Override
    public void requestBluetoothDiscoverability() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABILITY_DURATION_SEC);
        startActivityForResult(discoverableIntent, REQUEST_CODE_DISCOVERABILITY);
    }

    private BaseBluetoothPresenter<BaseBluetoothView> getBaseBluetoothPresenter() {
        return (BaseBluetoothPresenter<BaseBluetoothView>) getPresenter();
    }

}
