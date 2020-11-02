package org.pacific_emis.surveys.accreditation_core.di.modules;

import org.simpleframework.xml.core.Persister;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.data.serializers.AccreditationSurveyParser;
import org.pacific_emis.surveys.accreditation_core.data.serializers.AccreditationSurveySerializer;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreScope;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;

@Module
public class SerializersModule {

    @Provides
    @AccreditationCoreScope
    public SurveySerializer provideSurveySerializer(Persister persister) {
        return new AccreditationSurveySerializer(persister);
    }


    @Provides
    @AccreditationCoreScope
    public SurveyParser provideSurveyParser(Persister persister) {
        return new AccreditationSurveyParser(persister);
    }
}
