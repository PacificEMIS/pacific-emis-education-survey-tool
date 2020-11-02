package org.pacific_emis.surveys.report_core.di;

import dagger.Component;
import org.pacific_emis.surveys.core.di.CoreComponent;

@ReportCoreScope
@Component(modules = {
        ReportCoreModule.class
}, dependencies = {
        CoreComponent.class
})
public interface ReportCoreComponent {
}
