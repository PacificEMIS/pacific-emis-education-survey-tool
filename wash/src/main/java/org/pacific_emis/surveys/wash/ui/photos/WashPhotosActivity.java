package org.pacific_emis.surveys.wash.ui.photos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import org.pacific_emis.surveys.core.di.CoreComponentInjector;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentInjector;
import org.pacific_emis.surveys.survey_core.ui.photos.PhotosActivity;
import org.pacific_emis.surveys.survey_core.ui.photos.PhotosPresenter;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponentInjector;

public class WashPhotosActivity extends PhotosActivity implements WashPhotosView {

    @InjectPresenter
    WashPhotosPresenter presenter;

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, WashPhotosActivity.class);
    }

    @ProvidePresenter
    WashPhotosPresenter providePresenter() {
        Application application = getApplication();
        return new WashPhotosPresenter(
                CoreComponentInjector.getComponent(application),
                RemoteStorageComponentInjector.getComponent(application),
                WashCoreComponentInjector.getComponent(application)
        );
    }

    @Override
    protected PhotosPresenter getPresenter() {
        return presenter;
    }
}
