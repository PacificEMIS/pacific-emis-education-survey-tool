package fm.doe.national.di.modules;

import com.tickaroo.tikxml.TikXml;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TikXmlModule {

    @Provides
    @Singleton
    public TikXml provideTikXml() {
        return new TikXml.Builder()
                .writeDefaultXmlDeclaration(false)
                .exceptionOnUnreadXml(false)
                .build();

    }
}
