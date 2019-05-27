package fm.doe.national.app_support.di.modules;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.domain.SettingsInteractor;

@Module
public class InteractorsModule {

    @Provides
    public SettingsInteractor provideSettingsInteractor(CloudRepository cloudRepository,
                                                        DataSource localDataRepository,
                                                        Parser<Survey> surveyParser,
                                                        Parser<List<School>> schoolsParser) {
        return new SettingsInteractor(cloudRepository, localDataRepository, surveyParser, schoolsParser);
    }

}
