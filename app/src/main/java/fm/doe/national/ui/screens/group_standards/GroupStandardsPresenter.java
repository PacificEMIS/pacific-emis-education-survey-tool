package fm.doe.national.ui.screens.group_standards;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.ModelsExt;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class GroupStandardsPresenter extends BasePresenter<GroupStandardsView> {
    private SchoolAccreditationPassing schoolAccreditationPassing;
    private long schoolAccreditationPassingId;

    @Inject
    DataSource dataSource;

    public GroupStandardsPresenter(long schoolAccreditationPassingId) {
        this.schoolAccreditationPassingId = schoolAccreditationPassingId;
        MicronesiaApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        load();
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToStandardScreen(0, 0);
    }

    public void onGroupClicked(GroupStandard group) {
        extractStandardsOf(group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    add(disposable);
                    getViewState().showWaiting();
                })
                .doOnSuccess(standards -> {
                    if (standards.size() > 1) {
                        getViewState().showStandards(standards);
                    } else {
                        onStandardClicked(standards.get(0));
                    }
                })
                .doOnError(throwable -> getViewState().showWarning(
                        Text.from(R.string.title_warning),
                        Text.from(R.string.warn_unable_to_get_schools)))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe();
    }

    // TODO: replace
    private void load() {
        dataSource.requestSchoolAccreditationPassings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    add(disposable);
                    getViewState().showWaiting();
                })
                .doOnSuccess(schoolAccreditationPassings -> {
                    schoolAccreditationPassing = schoolAccreditationPassings.get(0); // FIXME: temp
                    loadStandards();
                })
                .doOnError(throwable -> getViewState().showWarning(
                        Text.from(R.string.title_warning),
                        Text.from(R.string.warn_unable_to_get_schools)))
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe();
    }

    private void loadStandards() {
        List<GroupStandard> groups = new ArrayList<>(schoolAccreditationPassing.getSchoolAccreditation().getGroupStandards());
        List<Standard> standards = new ArrayList<>();
        for (GroupStandard groupStandard : groups) {
            standards.addAll(groupStandard.getStandards());
        }
        getViewState().showGroupStandards(groups);

        int completedCount = 0;
        for (Standard standard : standards) {
            if (ModelsExt.getAnsweredQuestionsCount(standard) == ModelsExt.getTotalQuestionsCount(standard)) completedCount++;
        }
        getViewState().setGlobalProgress(completedCount, standards.size());
    }

    private Single<List<Standard>> extractStandardsOf(GroupStandard group) {
        return Single.fromCallable(() -> new ArrayList<>(group.getStandards())); // TODO: datasource
    }

}
