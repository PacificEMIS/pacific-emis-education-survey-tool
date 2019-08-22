package fm.doe.national.wash_core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.wash_core.data.data_source.WashDataSource;
import fm.doe.national.wash_core.di.WashCoreScope;
import fm.doe.national.wash_core.interactors.WashSurveyInteractor;
import fm.doe.national.wash_core.interactors.WashSurveyInteractorImpl;

@Module
public class InteractorsModule {

    @Provides
    @WashCoreScope
    WashSurveyInteractor provideAccreditationSurveyInteractor(WashDataSource dataSource, Context context) {
        return new WashSurveyInteractorImpl(dataSource, context);
    }
}
