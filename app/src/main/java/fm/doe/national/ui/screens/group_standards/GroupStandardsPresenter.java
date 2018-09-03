package fm.doe.national.ui.screens.group_standards;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class GroupStandardsPresenter extends BasePresenter<GroupStandardsView> {

    private long passingId;

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    public GroupStandardsPresenter(long passingId) {
        this.passingId = passingId;
    }

    @Override
    public void attachView(GroupStandardsView view) {
        super.attachView(view);
        loadPassing();
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToStandardScreen(passingId, standard.getId());
    }

    @SuppressLint("CheckResult")
    public void onGroupClicked(GroupStandard group) {
        dataSource.requestStandards(passingId, group.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                    getViewState().showWaiting();
                })
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(standards -> {
                    if (standards.size() > 1) {
                        getViewState().showStandards(standards);
                    } else {
                        onStandardClicked(standards.get(0));
                    }
                }, this::handleError);
    }

    @SuppressLint("CheckResult")
    private void loadPassing() {
        dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    addDisposable(disposable);
                    getViewState().showWaiting();
                })
                .doOnSuccess(passing -> {
                    GroupStandardsView view = getViewState();
                    view.setSurveyYear(passing.getYear());
                    view.setSchoolName(passing.getSchool().getName());

                    CategoryProgress progress = passing.getSchoolAccreditation().getCategoryProgress();
                    view.setGlobalProgress(progress.getAnsweredQuestionsCount(), progress.getTotalQuestionsCount());
                })
                .flatMap(passing -> dataSource.requestGroupStandards(passingId))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(groupStandards -> getViewState().showGroupStandards(groupStandards), this::handleError);
    }

}
