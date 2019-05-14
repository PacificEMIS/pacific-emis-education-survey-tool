package fm.doe.national.ui.screens.base;

import com.omegar.mvp.MvpPresenter;

import fm.doe.national.app_support.utils.DisposeBag;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

class BaseDisposablePresenter<T extends BaseView> extends MvpPresenter<T> {

    private DisposeBag disposeBag = new DisposeBag();

    public BaseDisposablePresenter() {

    }

    protected void addDisposable(@NonNull Disposable d) {
        disposeBag.add(d);
    }

    @Override
    public void onDestroy() {
        disposeBag.dispose();
        super.onDestroy();
    }

}
