package fm.doe.national.fcm_report.di;

import dagger.Component;

@Component(modules = {
        DomainModule.class
})
public interface FcmReportComponent {
    void inject(/*presenter*/);
}
