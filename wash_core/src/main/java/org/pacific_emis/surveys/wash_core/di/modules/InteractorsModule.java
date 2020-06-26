package org.pacific_emis.surveys.wash_core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.wash_core.data.data_source.WashDataSource;
import org.pacific_emis.surveys.wash_core.di.WashCoreScope;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractor;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractorImpl;

@Module
public class InteractorsModule {

    @Provides
    @WashCoreScope
    WashSurveyInteractor provideAccreditationSurveyInteractor(WashDataSource dataSource, Context context) {
        return new WashSurveyInteractorImpl(dataSource, context);
    }
}
