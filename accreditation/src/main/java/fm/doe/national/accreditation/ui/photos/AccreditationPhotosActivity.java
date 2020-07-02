package fm.doe.national.accreditation.ui.photos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.core.di.CoreComponentInjector;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.survey_core.ui.photos.PhotosActivity;
import fm.doe.national.survey_core.ui.photos.PhotosPresenter;

public class AccreditationPhotosActivity extends PhotosActivity implements AccreditationPhotosView {

    @InjectPresenter
    AccreditationPhotosPresenter presenter;

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, AccreditationPhotosActivity.class);
    }

    @ProvidePresenter
    AccreditationPhotosPresenter providePresenter() {
        Application application = getApplication();
        return new AccreditationPhotosPresenter(
                CoreComponentInjector.getComponent(application),
                RemoteStorageComponentInjector.getComponent(application),
                AccreditationCoreComponentInjector.getComponent(application)
        );
    }

    @Override
    protected PhotosPresenter getPresenter() {
        return presenter;
    }
}
