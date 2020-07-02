package fm.doe.national.accreditation_core.interactors;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.core.domain.SurveyInteractor;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface AccreditationSurveyInteractor extends SurveyInteractor {

    Single<List<MutableCategory>> requestCategories();

    Single<List<MutableStandard>> requestStandards(long categoryId);

    Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId);

    Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId);

    Completable updateAnswer(Answer answer);

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

}
