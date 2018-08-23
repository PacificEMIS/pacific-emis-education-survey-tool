package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.view_data.CriteriaViewData;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
import io.reactivex.Observable;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    @Inject
    DataSource dataSource;

    private SchoolAccreditationPassing accreditationResult;
    private int standardIndex;
    private List<Standard> standards;
    private List<CriteriaViewData> criteriaViewDataList;

    public StandardPresenter(SchoolAccreditationPassing result) {
        MicronesiaApplication.getAppComponent().inject(this);

        this.accreditationResult = result;
        this.standardIndex = 0;
        standards = new ArrayList<>();

        // TODO: handle groupStandards properly

        for (GroupStandard groupStandard: accreditationResult.getSchoolAccreditation().getGroupStandards()) {
            standards.addAll(groupStandard.getStandards());
        }

        updateUi();
    }

    public void onQuestionStateChanged(@NonNull SubCriteriaViewData subCriteriaViewData,
                                       @NonNull SubCriteria subCriteria,
                                       @Nullable Answer answer, Answer.State newState) {
        if (answer != null) {
            if (newState == Answer.State.NOT_ANSWERED) {
                // FIXME: rewrite answers logic
                //dataSource.deleteAnswer(answer);
            }
            answer.setAnswer(newState == Answer.State.POSITIVE);
            add(dataSource.updateAnswer(answer)
                    .subscribe(() -> {
                        //nothing
                    }, throwable -> {
                        //nothing
                    }));
        } else {
            add(dataSource.createAnswer(newState == Answer.State.POSITIVE, subCriteria, accreditationResult)
                    .doOnSuccess(subCriteriaViewData::setCorrespondingAnswer)
                    .subscribe());
        }
        getViewState().setProgress(getAnsweredCount(), getQuestionsCount());
    }

    public void onNextPressed() {
        standardIndex = getNextIndex();
        updateUi();
    }

    public void onPreviousPressed() {
        standardIndex = getPrevIndex();
        updateUi();
    }

    private void updateUi() {
        getViewState().setGlobalInfo(standards.get(standardIndex).getName(), standardIndex);
        getViewState().setPrevStandard(standards.get(getPrevIndex()).getName(), getPrevIndex());
        getViewState().setNextStandard(standards.get(getNextIndex()).getName(), getNextIndex());

        loadQuestions();
    }

    private void loadQuestions() {
        add(Observable
                .fromIterable(standards.get(standardIndex).getCriterias())
                .concatMap(criteria -> dataSource.requestAnswers(criteria, accreditationResult)
                        .map(children -> Pair.create(criteria, children))
                        .toObservable())
                .toMap(mapPair -> mapPair.first, mapPair -> mapPair.second)
                .doOnSuccess((Map<? extends Criteria, Map<SubCriteria, Answer>> criteriasQuestions) -> {
                    criteriaViewDataList = convertToViewData(criteriasQuestions);
                    getViewState().setCriterias(criteriaViewDataList);
                    getViewState().setProgress(getAnsweredCount(), getQuestionsCount());
                })
                .subscribe());
    }

    @NonNull
    private List<CriteriaViewData> convertToViewData(Map<? extends Criteria, Map<SubCriteria, Answer>> raw) {
        List<CriteriaViewData> result = new ArrayList<>();
        for (Map.Entry<? extends Criteria, Map<SubCriteria, Answer>> entryCriteria : raw.entrySet()) {
            CriteriaViewData.Builder viewDataBuilder = new CriteriaViewData.Builder(entryCriteria.getKey());
            for (Map.Entry<SubCriteria, Answer> entrySubCriteria: entryCriteria.getValue().entrySet()) {
                viewDataBuilder.addQuestion(entrySubCriteria.getKey(), entrySubCriteria.getValue());
            }
            result.add(viewDataBuilder.build());
        }
        return result;
    }

    private int getNextIndex() {
        return standardIndex < standards.size() - 1 ? standardIndex + 1 : 0;
    }

    private int getPrevIndex() {
        return standardIndex > 0 ? standardIndex - 1 : standards.size() - 1;
    }

    private int getAnsweredCount() {
        if (criteriaViewDataList == null) return 0;
        int count = 0;
        for (CriteriaViewData criteriaViewData: criteriaViewDataList) {
            for (SubCriteriaViewData subCriteriaViewData: criteriaViewData.getQuestionsViewData()) {
                if (subCriteriaViewData.getAnswer() != Answer.State.NOT_ANSWERED) count++;
            }
        }
        return count;
    }

    private int getQuestionsCount() {
        int count = 0;
        for (Standard standard: standards) {
            count += standard.getCriterias().size();
        }
        return count;
    }
}
