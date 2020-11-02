package org.pacific_emis.surveys.accreditation_core.data.data_source;

import org.pacific_emis.surveys.accreditation_core.data.model.Answer;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.core.data.data_source.DataSource;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AccreditationDataSource extends DataSource {

    Single<Answer> updateAnswer(Answer answer);

    Completable updateObservationInfo(ObservationInfo observationInfo, long categoryId);

    Single<List<MutableObservationLogRecord>> getLogRecordsForCategoryWithId(long categoryId);

    Completable updateObservationLogRecord(ObservationLogRecord record);

    Completable deleteObservationLogRecord(long recordId);

    Single<MutableObservationLogRecord> createEmptyLogRecord(long categoryId, Date date);

    Completable createLogRecords(long categoryId, List<? extends ObservationLogRecord> records);

}
