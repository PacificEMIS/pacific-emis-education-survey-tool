package fm.doe.national.accreditation.di;

import dagger.Component;
import fm.doe.national.accreditation.di.modules.AccreditationSourceModule;
import fm.doe.national.core.di.CoreComponent;

@AccreditationScope
@Component(modules = {
        AccreditationSourceModule.class
}, dependencies = {
        CoreComponent.class
})
public interface AccreditationComponent {
}
