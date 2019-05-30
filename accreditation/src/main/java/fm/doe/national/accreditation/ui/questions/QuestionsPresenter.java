package fm.doe.national.accreditation.ui.questions;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class QuestionsPresenter extends BasePresenter<QuestionsView> {

    private final SurveyInteractor surveyInteractor;
    private final long standardId;
    private final long categoryId;

    QuestionsPresenter(CoreComponent coreComponent, long categoryId, long standardId) {
        this.surveyInteractor = coreComponent.getSurveyInteractor();
        this.standardId = standardId;
        this.categoryId = categoryId;
        loadQuestions();
    }

    private void loadQuestions() {
        addDisposable(
                surveyInteractor.requestCriterias(categoryId, standardId)
                        .flatMap(criteriaList -> Single.fromCallable(() -> {
                            List<Question> questions = new ArrayList<>();
                            criteriaList.forEach(criteria -> {
                                        questions.add(new Question(criteria));
                                        questions.addAll(
                                                criteria.getSubCriterias()
                                                        .stream()
                                                        .map(Question::new)
                                                        .collect(Collectors.toList())
                                        );
                                    }
                            );
                            return questions;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setQuestions, this::handleError)
        );
    }

    void onCommentPressed(Question question) {
        getViewState().showCommentEditor(question.getSubCriteria().getAnswer().getComment());
    }

    void onPhotosPressed() {
        getViewState().showPhotos();
    }

    void onAnswerChanged(Question updatedQuestion) {

    }
}
