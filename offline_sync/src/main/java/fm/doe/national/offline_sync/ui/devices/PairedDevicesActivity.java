package fm.doe.national.offline_sync.ui.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;

import fm.doe.national.offline_sync.R;
import fm.doe.national.offline_sync.data.accessor.BluetoothOfflineAccessor;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponentInjector;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothActivity;

public class PairedDevicesActivity extends BaseBluetoothActivity implements
        PairedDevicesView,
        BaseListAdapter.OnItemClickListener<Device>,
        SwipeRefreshLayout.OnRefreshListener {

    private final PairedDevicesAdapter adapter = new PairedDevicesAdapter(this);
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.onBroadcastReceive(context, intent);
        }
    };

    @InjectPresenter
    PairedDevicesPresenter presenter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, PairedDevicesActivity.class);
    }

    @ProvidePresenter
    PairedDevicesPresenter providePresenter() {
        return new PairedDevicesPresenter(OfflineSyncComponentInjector.getComponent(getApplication()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_devices);
        bindViews();
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        BluetoothOfflineAccessor.sReceiverActionsToRegister.forEach(filter::addAction);
        registerReceiver(broadcastReceiver, filter);
    }

    private void bindViews() {
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_devices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open_settings) {
            presenter.onOpenSettingsPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_paired_devices;
    }

    @Override
    public void setDevicesList(List<Device> devices) {
        adapter.setItems(devices);
    }

    @Override
    public void navigateToDeviceSettings() {
        startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
    }

    @Override
    public void onItemClick(Device item) {
        presenter.onDevicePressed(item);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void showWaiting() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideWaiting() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Nullable
    @Override
    public PairedDevicesPresenter getPresenter() {
        return presenter;
    }
}
