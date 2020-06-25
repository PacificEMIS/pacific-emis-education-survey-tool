package fm.doe.national.accreditation_core.interactors;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.ObservationLogRecord;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.core.domain.SurveyInteractor;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface AccreditationSurveyInteractor extends SurveyInteractor {

    Single<List<MutableCategory>> requestCategories();

    Single<List<MutableStandard>> requestStandards(long categoryId);

    Single<MutableCategory> requestCategory(long categoryId);

    Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId);

    Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId);

    Completable updateAnswer(Answer answer);

    Completable updateClassroomObservationInfo(ObservationInfo info, long categoryId);

    Observable<MutableCategory> getCategoryProgressObservable();

    Observable<MutableStandard> getStandardProgressObservable();

    Observable<MutableCriteria> getCriteriaProgressObservable();

    long getCurrentCategoryId();

    long getCurrentStandardId();

    long getCurrentCriteriaId();

    long getCurrentSubCriteriaId();

    void setCurrentCategoryId(long id);

    void setCurrentStandardId(long id);

    void setCurrentCriteriaId(long id);

    void setCurrentSubCriteriaId(long id);

    Single<List<MutableObservationLogRecord>> requestLogRecords(long categoryId);

    Single<MutableObservationLogRecord> createEmptyLogRecord(long categoryId);

    Completable updateObservationLogRecord(ObservationLogRecord record);

    Completable deleteObservationLogRecord(long recordId);

}
