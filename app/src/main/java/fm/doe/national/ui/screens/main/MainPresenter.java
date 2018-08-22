package fm.doe.national.ui.screens.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.base.BaseView;
import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class MainPresenter extends BasePresenter<BaseView> {

    @Inject
    CloudRepository repository;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        MicronesiaApplication.getAppComponent().inject(this);
        repository.uploadContent("HELLO WORLD")
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d("DEBIG", "SUCCESS"))
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }
}
