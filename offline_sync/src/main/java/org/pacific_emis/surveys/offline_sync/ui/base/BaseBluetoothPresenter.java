package org.pacific_emis.surveys.offline_sync.ui.base;

import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.offline_sync.data.accessor.OfflineAccessor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseBluetoothPresenter<T extends BaseBluetoothView> extends BasePresenter<T> {

    protected final OfflineAccessor offlineAccessor;

    @Nullable
    private Action permissionAction;

    @Nullable
    private Action discoverabilityAction;

    public BaseBluetoothPresenter(OfflineAccessor offlineAccessor) {
        this.offlineAccessor = offlineAccessor;

        addDisposable(
                offlineAccessor.getPermissionsRequestObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    permissionAction = action;
                    getViewState().requestBluetoothPermissions();
                }, this::handleError)
        );

        addDisposable(
                offlineAccessor.getDiscoverableRequestObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    discoverabilityAction = action;
                    getViewState().requestBluetoothDiscoverability();
                }, this::handleError)
        );
    }

    public void onBluetoothPermissionsGranted() {
        if (permissionAction != null) {
            try {
                permissionAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBecomeDiscoverable() {
        if (discoverabilityAction != null) {
            try {
                discoverabilityAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
