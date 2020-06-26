package org.pacific_emis.surveys.remote_settings.di;

import dagger.Component;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.remote_settings.model.RemoteSettings;

@RemoteSettingsScope
@Component(modules = {
        RemoteSettingsModule.class
}, dependencies = {
        CoreComponent.class
})
public interface RemoteSettingsComponent {
    RemoteSettings getRemoteSettings();
}
