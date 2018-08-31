package fm.doe.national.ui.screens.base;

import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.OpenHashSet;

class BaseDisposablePresenter<T extends BaseView> extends MvpPresenter<T> {

    private OpenHashSet<Disposable> resources;

    private volatile boolean disposed;

    public BaseDisposablePresenter() {

    }

    public BaseDisposablePresenter(@NonNull Disposable... resources) {
        this.resources = new OpenHashSet<>(resources.length + 1);
        for (Disposable d : resources) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public BaseDisposablePresenter(@NonNull Iterable<? extends Disposable> resources) {
        this.resources = new OpenHashSet<>();
        for (Disposable d : resources) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    @Deprecated
    protected void add(@NonNull Disposable d){
        addDisposable(d);
    }

    protected void addDisposable(@NonNull Disposable d) {
        if (!disposed) {
            synchronized (this) {
                if (!disposed) {
                    OpenHashSet<Disposable> set = resources;
                    if (set == null) {
                        set = new OpenHashSet<>();
                        resources = set;
                    }
                    set.add(d);
                    return;
                }
            }
        }
        d.dispose();
    }

    @Override
    public void onDestroy() {
        dispose();
        super.onDestroy();
    }

    private void dispose() {
        if (disposed) {
            return;
        }
        OpenHashSet<Disposable> set;
        synchronized (this) {
            if (disposed) {
                return;
            }
            disposed = true;
            set = resources;
            resources = null;
        }

        dispose(set);
    }

    private void dispose(OpenHashSet<Disposable> set) {
        if (set == null) {
            return;
        }
        List<Throwable> errors = null;
        Object[] array = set.keys();
        for (Object o : array) {
            if (o instanceof Disposable) {
                try {
                    ((Disposable) o).dispose();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(ex);
                }
            }
        }
        if (errors != null) {
            if (errors.size() == 1) {
                throw ExceptionHelper.wrapOrThrow(errors.get(0));
            }
            throw new CompositeException(errors);
        }
    }

}
