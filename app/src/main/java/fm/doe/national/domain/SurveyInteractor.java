package fm.doe.national.domain;

import java.util.List;

import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSurvey;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public interface SurveyInteractor {

    Single<List<MutableSurvey>> getAllSurveys();

    void setCurrentSurvey(MutableSurvey survey);

    Single<List<MutableCategory>> getCategories();

    Single<List<MutableStandard>> getStandards(long categoryId);

    Single<List<MutableCriteria>> getCriterias(long categoryId, long standardId);

    Single<Object> getSummaryObject();

    void updateAnswer(MutableAnswer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId);

    PublishSubject<MutableSurvey> getSurveyProgressSubject();

    PublishSubject<MutableCategory> getCategoryProgressSubject();

    PublishSubject<MutableStandard> getStandardProgressSubject();

    PublishSubject<MutableCriteria> getCriteriaProgressSubject();

}
