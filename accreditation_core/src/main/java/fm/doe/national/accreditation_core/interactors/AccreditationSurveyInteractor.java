package fm.doe.national.accreditation_core.interactors;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.core.domain.SurveyInteractor;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public interface AccreditationSurveyInteractor extends SurveyInteractor {

    Single<List<MutableCategory>> requestCategories();

    Single<List<MutableStandard>> requestStandards(long categoryId);

    Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId);

    Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId);

    PublishSubject<MutableCategory> getCategoryProgressSubject();

    PublishSubject<MutableStandard> getStandardProgressSubject();

    PublishSubject<MutableCriteria> getCriteriaProgressSubject();

}
