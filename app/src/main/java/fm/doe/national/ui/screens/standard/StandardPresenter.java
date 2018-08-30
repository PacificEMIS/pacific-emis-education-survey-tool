package fm.doe.national.ui.screens.standard;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    @Inject
    DataSource dataSource;

    private long schoolAccreditationPassingId;
    private int standardIndex;
    private int nextIndex;
    private int previousIndex;
    private List<Standard> standards = new ArrayList<>();

    public StandardPresenter(long passingId, long standardId, long[] groupsIds) {
        MicronesiaApplication.getAppComponent().inject(this);
        schoolAccreditationPassingId = passingId;
        load(standardId);
    }

    @SuppressLint("CheckResult")
    public void onSubCriteriaStateChanged(SubCriteria subCriteria) {
        Answer answer = subCriteria.getAnswer();

        dataSource.updateAnswer(schoolAccreditationPassingId, subCriteria.getId(), answer.getState())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::add)
                .subscribe(() -> {}, this::handleError);

        CategoryProgress categoryProgress = standards.get(standardIndex).getCategoryProgress();
        if (answer.getState() == Answer.State.NOT_ANSWERED) {
            categoryProgress.decrementCompletedItems();
        } else {
            categoryProgress.incrementCompletedItems();
        }

        updateProgress();
    }

    private void updateProgress() {
        CategoryProgress categoryProgress = standards.get(standardIndex).getCategoryProgress();
        getViewState().setProgress(
                categoryProgress.getCompletedItemsCount(),
                categoryProgress.getTotalItemsCount());
    }

    public void onNextPressed() {
        previousIndex = standardIndex;
        standardIndex = nextIndex;
        nextIndex = getNextIndex();
        updateUi();
    }

    public void onPreviousPressed() {
        nextIndex = standardIndex;
        standardIndex = previousIndex;
        nextIndex = getPrevIndex();
        updateUi();
    }

    private void updateUi() {
        getViewState().setGlobalInfo(standards.get(standardIndex).getName(), standardIndex);
        getViewState().setPrevStandard(standards.get(previousIndex).getName(), previousIndex);
        getViewState().setNextStandard(standards.get(nextIndex).getName(), nextIndex);

        loadQuestions();
    }

    @SuppressLint("CheckResult")
    private void loadQuestions() {
        long standardId = standards.get(standardIndex).getId();
        dataSource.requestCriterias(schoolAccreditationPassingId, standardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showWaiting();
                    add(disposable);
                })
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(criterias -> {
                    getViewState().setCriterias(criterias);
                    updateProgress();
                }, this::handleError);
    }

    private int getNextIndex() {
        return standardIndex < standards.size() - 1 ? standardIndex + 1 : 0;
    }

    private int getPrevIndex() {
        return standardIndex > 0 ? standardIndex - 1 : standards.size() - 1;
    }

    // TODO: replace
    @SuppressLint("CheckResult")
    private void load(long standardId) {
        dataSource.requestStandards(schoolAccreditationPassingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showWaiting();
                    add(disposable);
                })
                .doOnSuccess(standards -> this.standards = standards)
                .flatMap(standards -> dataSource.requestStandard(schoolAccreditationPassingId, standardId))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(standard -> {
                    standardIndex = standards.indexOf(standard);
                    nextIndex = getNextIndex();
                    previousIndex = getPrevIndex();
                    updateUi();
                }, this::handleError);
    }
}
