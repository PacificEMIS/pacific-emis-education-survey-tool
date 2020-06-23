package fm.doe.national.accreditation_core.data.data_source;

import java.util.Date;
import java.util.List;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.ObservationLogRecord;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.core.data.data_source.DataSource;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface AccreditationDataSource extends DataSource {

    Single<Answer> updateAnswer(Answer answer);

    Completable updateObservationInfo(ObservationInfo observationInfo, long categoryId);

    Single<List<MutableObservationLogRecord>> getLogRecordsForCategoryWithId(long categoryId);

    Completable updateObservationLogRecord(ObservationLogRecord record);

    Completable deleteObservationLogRecord(long recordId);

    Single<MutableObservationLogRecord> createEmptyLogRecord(long categoryId, Date date);
}
