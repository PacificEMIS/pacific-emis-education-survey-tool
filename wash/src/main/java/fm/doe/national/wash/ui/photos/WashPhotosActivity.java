package fm.doe.national.wash.ui.photos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.cloud.di.CloudComponentInjector;
import fm.doe.national.core.di.CoreComponentInjector;
import fm.doe.national.survey_core.ui.photos.PhotosActivity;
import fm.doe.national.survey_core.ui.photos.PhotosPresenter;
import fm.doe.national.wash_core.di.WashCoreComponentInjector;

public class WashPhotosActivity extends PhotosActivity {

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
                CloudComponentInjector.getComponent(application),
                WashCoreComponentInjector.getComponent(application)
        );
    }

    @Override
    protected PhotosPresenter getPresenter() {
        return presenter;
    }
}
