package fm.doe.national.accreditation.ui.photos;

import android.app.Application;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.cloud.di.CloudComponentInjector;
import fm.doe.national.core.di.CoreComponentInjector;
import fm.doe.national.survey_core.ui.photos.PhotosActivity;
import fm.doe.national.survey_core.ui.photos.PhotosPresenter;

public class AccreditationPhotosActivity extends PhotosActivity {

    @InjectPresenter
    AccreditationPhotosPresenter presenter;

    @ProvidePresenter
    AccreditationPhotosPresenter providePresenter() {
        Application application = getApplication();
        return new AccreditationPhotosPresenter(
                CoreComponentInjector.getComponent(application),
                CloudComponentInjector.getComponent(application),
                AccreditationCoreComponentInjector.getComponent(application)
        );
    }

    @Override
    protected PhotosPresenter getPresenter() {
        return presenter;
    }
}
