package fm.doe.national.wash_core.interactors;

import java.util.List;

import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.mutable.MutableGroup;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;
import fm.doe.national.wash_core.data.model.mutable.MutableSubGroup;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface WashSurveyInteractor extends SurveyInteractor {

    Single<List<MutableGroup>> requestGroups();

    Single<List<MutableSubGroup>> requestSubGroups(long groupId);

    Single<List<MutableQuestion>> requestQuestions(long groupId, long subGroupId);

    Completable updateAnswer(Answer answer, long groupId, long subGroupId, long questionId);

    Completable updateAnswer(Answer answer);

    Observable<MutableGroup> getGroupProgressObservable();

    Observable<MutableSubGroup> getSubGroupProgressObservable();

    long getCurrentGroupId();

    long getCurrentSubGroupId();

    long getCurrentQuestionId();

    void setCurrentGroupId(long id);

    void setCurrentSubGroupId(long id);

    void setCurrentQuestionId(long id);

}
