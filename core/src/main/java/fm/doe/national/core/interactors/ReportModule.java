package fm.doe.national.core.interactors;

import java.util.List;

import fm.doe.national.core.data.model.ReportPage;
import fm.doe.national.core.data.model.Survey;

public interface ReportModule {

    void requestReports(Survey survey);

    List<ReportPage> getPages();

}
