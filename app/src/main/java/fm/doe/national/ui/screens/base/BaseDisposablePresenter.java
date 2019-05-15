package fm.doe.national.ui.screens.base;

import com.omegar.mvp.MvpPresenter;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

class BaseDisposablePresenter<T extends BaseView> extends MvpPresenter<T> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseDisposablePresenter() {
        // nothing
    }

    protected void addDisposable(@NonNull Disposable d) {
        compositeDisposable.add(d);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

}
