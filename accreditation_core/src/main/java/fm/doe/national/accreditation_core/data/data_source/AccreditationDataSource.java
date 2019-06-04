package fm.doe.national.accreditation_core.data.data_source;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.core.data.data_source.DataSource;
import io.reactivex.Single;

public interface AccreditationDataSource extends DataSource {

    Single<Answer> updateAnswer(Answer answer, long subCriteriaId);

}
