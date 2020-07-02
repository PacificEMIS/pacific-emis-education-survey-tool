package fm.doe.national.report_core.di;

import dagger.Component;
import fm.doe.national.core.di.CoreComponent;

@ReportCoreScope
@Component(modules = {
        ReportCoreModule.class
}, dependencies = {
        CoreComponent.class
})
public interface ReportCoreComponent {
}
