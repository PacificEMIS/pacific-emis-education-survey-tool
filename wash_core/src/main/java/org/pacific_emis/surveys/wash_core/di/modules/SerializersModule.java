package org.pacific_emis.surveys.wash_core.di.modules;

import org.simpleframework.xml.core.Persister;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.wash_core.data.serialization.serializers.WashSurveyParser;
import org.pacific_emis.surveys.wash_core.data.serialization.serializers.WashSurveySerializer;
import org.pacific_emis.surveys.wash_core.di.WashCoreScope;

@Module
public class SerializersModule {

    @Provides
    @WashCoreScope
    public SurveySerializer provideSurveySerializer(Persister persister) {
        return new WashSurveySerializer(persister);
    }

    @Provides
    @WashCoreScope
    public SurveyParser provideSurveyParser(Persister persister) {
        return new WashSurveyParser(persister);
    }
}
