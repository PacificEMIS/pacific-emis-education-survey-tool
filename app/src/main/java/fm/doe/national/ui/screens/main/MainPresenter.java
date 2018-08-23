package fm.doe.national.ui.screens.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.base.BaseView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends BasePresenter<BaseView> {

    @Inject
    CloudRepository repository;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        MicronesiaApplication.getAppComponent().inject(this);
        repository.setPrimary(CloudAccessor.Type.DROPBOX);
        add(repository.uploadContent("HELLO WORLD")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("SAD", "asdads"),
                        throwable -> Log.d("aasda","asdasd"))
        );
    }
}
