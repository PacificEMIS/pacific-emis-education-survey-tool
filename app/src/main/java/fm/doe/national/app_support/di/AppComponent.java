package fm.doe.national.app_support.di;

import dagger.Component;
import fm.doe.national.app_support.di.modules.InteractorsModule;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.domain.SettingsInteractor;

@AppScope
@Component(modules = {
        InteractorsModule.class,
}, dependencies = {
        CoreComponent.class
})
public interface AppComponent {

    SettingsInteractor getSettingsInteractor();

}
