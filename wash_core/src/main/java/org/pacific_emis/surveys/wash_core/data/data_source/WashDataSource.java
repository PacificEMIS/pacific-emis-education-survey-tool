package org.pacific_emis.surveys.wash_core.data.data_source;

import org.pacific_emis.surveys.core.data.data_source.DataSource;
import org.pacific_emis.surveys.wash_core.data.model.Answer;
import io.reactivex.Single;

public interface WashDataSource extends DataSource {

    Single<Answer> updateAnswer(Answer answer);

    Single<Answer> createAnswer(Answer answer, long questionId);

}
