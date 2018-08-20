package fm.doe.national.data.cloud.accessors;

import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.data.data_source.models.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DriveCloudAccessor implements CloudAccessor {
    @Override
    public Completable export(Collection<SchoolAccreditationResult> accreditationResults) {
        return null;
    }

    @Override
    public Completable debugExport(String text) {
        return null;
    }

    @Override
    public Single<String> debugImport() {
        return null;
    }

    @Override
    public Single<List<School>> importSchools() {
        return null;
    }

    @Override
    public Single<Survey> importSurvey() {
        return null;
    }
}
