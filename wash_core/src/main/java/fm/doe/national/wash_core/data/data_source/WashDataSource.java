package fm.doe.national.wash_core.data.data_source;

import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.wash_core.data.model.Answer;
import io.reactivex.Single;

public interface WashDataSource extends DataSource {

    Single<Answer> updateAnswer(Answer answer);

}
