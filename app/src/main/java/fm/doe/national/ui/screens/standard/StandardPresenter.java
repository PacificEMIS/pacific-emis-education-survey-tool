package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.ModelsExt;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.view_data.CriteriaViewData;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    @Inject
    DataSource dataSource;

    private SchoolAccreditationPassing accreditationResult;
    private int standardIndex;
    private int nextIndex = standardIndex + 1;
    private int previousIndex;
    private List<Standard> standards = new ArrayList<>();
    private List<CriteriaViewData> criteriaViewDataList;

    public StandardPresenter(long passingId) {
        MicronesiaApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        load();
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
//            add(dataSource.updateAnswer(answer)
//                    .subscribe(() -> {
//                        //nothing
//                    }, throwable -> {
//                        getViewState().showWarning(
//                                Text.from(R.string.title_warning),
//                                Text.from(R.string.warn_unable_to_update_answer));
//                    }));
        } else {
//            add(dataSource.createAnswer(newState == Answer.State.POSITIVE, subCriteria, accreditationResult)
//                    .subscribe(
//                            subCriteriaViewData::setCorrespondingAnswer,
//                            throwable -> {
//                                getViewState().showWarning(
//                                        Text.from(R.string.title_warning),
//                                        Text.from(R.string.warn_unable_to_create_answer));
//                            }));
        }
        getViewState().setProgress(
                ModelsExt.getAnsweredQuestionsCount(standards.get(standardIndex)),
                ModelsExt.getTotalQuestionsCount(standards.get(standardIndex)));
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

    private void loadQuestions() { // FIXME
        Single.fromCallable(() -> {
            Map<Criteria, Map<SubCriteria, Answer>> criteriasQuestions = new HashMap<>();
            for (Criteria criteria : standards.get(standardIndex).getCriterias()) {
                Map<SubCriteria, Answer> answersMap = new HashMap<>();
                for (SubCriteria subCriteria : criteria.getSubCriterias()) {
                    answersMap.put(subCriteria, null);
                }
                criteriasQuestions.put(criteria, answersMap);
            }
            return criteriasQuestions;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showProgressDialog(Text.empty());
                    add(disposable);
                })
                .doOnSuccess(criteriasQuestions -> {
                    criteriaViewDataList = convertToViewData(criteriasQuestions);
                    getViewState().setCriterias(criteriaViewDataList);
                    getViewState().setProgress(
                            ModelsExt.getAnsweredQuestionsCount(standards.get(standardIndex)),
                            ModelsExt.getTotalQuestionsCount(standards.get(standardIndex)));
                })
                .doOnError(throwable -> {
                    getViewState().showWarning(
                            Text.from(R.string.title_warning),
                            Text.from(R.string.warn_unable_to_get_schools));
                    getViewState().hideProgressDialog();
                })
                .doFinally(() -> getViewState().hideProgressDialog())
                .subscribe();
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

    @Deprecated
    private int getAnsweredCount() {
        if (criteriaViewDataList == null) return 0;
        int count = 0;
        for (CriteriaViewData criteriaViewData: criteriaViewDataList) {
            count += criteriaViewData.getAnsweredCount();
        }
        return count;
    }

    // TODO: replace
    private void load() {
        dataSource.requestSchoolAccreditationPassings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getViewState().showProgressDialog(Text.empty());
                    add(disposable);
                })
                .doOnSuccess(schoolAccreditationPassings -> {
                    accreditationResult = schoolAccreditationPassings.get(0);
                    getViewState().hideProgressDialog();
                    loadStandards();
                })
                .doOnError(throwable -> {
                    getViewState().showWarning(
                            Text.from(R.string.title_warning),
                            Text.from(R.string.warn_unable_to_get_schools));
                    getViewState().hideProgressDialog();
                })
                .doFinally(() -> getViewState().hideProgressDialog())
                .subscribe();
    }

    private void loadStandards() {
        // TODO: handle groupStandards properly

        for (GroupStandard groupStandard: accreditationResult.getSchoolAccreditation().getGroupStandards()) {
            standards.addAll(groupStandard.getStandards());
        }

        previousIndex = standards.size() - 1;

        updateUi();
    }
}
