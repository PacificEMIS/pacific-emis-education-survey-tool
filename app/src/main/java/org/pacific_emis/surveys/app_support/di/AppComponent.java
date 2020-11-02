package org.pacific_emis.surveys.app_support.di;

import dagger.Component;
import org.pacific_emis.surveys.app_support.di.modules.InteractorsModule;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.domain.SettingsInteractor;

@AppScope
@Component(modules = {
        InteractorsModule.class,
}, dependencies = {
        CoreComponent.class
})
public interface AppComponent {

    SettingsInteractor getSettingsInteractor();

}
