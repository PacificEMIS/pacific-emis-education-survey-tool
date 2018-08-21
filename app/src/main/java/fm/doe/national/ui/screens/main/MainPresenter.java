package fm.doe.national.ui.screens.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.base.BaseView;
import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class MainPresenter extends BasePresenter<BaseView> {

    private final DriveCloudAccessor cloudAccessor = MicronesiaApplication.getAppComponent().getCloudAccessor();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        cloudAccessor.importContentFromCloud()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(content -> Log.d("CONTENT", content))
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }
}
