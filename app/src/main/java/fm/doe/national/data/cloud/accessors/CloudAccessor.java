package fm.doe.national.data.cloud.accessors;

import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.data.data_source.models.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudAccessor {
    Completable export(Collection<SchoolAccreditationResult> accreditationResults);
    Single<Survey> importSurvey();
    Single<List<School>> importSchools();
    Completable debugExport(String text);
    Single<String> debugImport();
}
