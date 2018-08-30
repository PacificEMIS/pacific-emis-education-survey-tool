package fm.doe.national.ui.screens.group_standards;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    private Long[] groupStandardsIds;

    @Inject
    DataSource dataSource;

    public GroupStandardsPresenter(long passingId) {
        MicronesiaApplication.getAppComponent().inject(this);
        this.passingId = passingId;
    }

    @Override
    public void attachView(GroupStandardsView view) {
        super.attachView(view);
        load();
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToStandardScreen(passingId, standard.getId(), groupStandardsIds);
    }

    public void onGroupClicked(GroupStandard group) {
        dataSource.requestStandards(passingId, group.getId())
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
                .doOnError(this::handleError)
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    private void load() {
        dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    add(disposable);
                    getViewState().showWaiting();
                })
                .doOnSuccess(passing -> {
                    CategoryProgress progress = passing.getSchoolAccreditation().getProgress();
                    getViewState().setGlobalProgress(progress.getCompletedItemsCount(), progress.getTotalItemsCount());

                })
                .flatMap(passing -> dataSource.requestGroupStandards(passingId))
                .doOnError(this::handleError)
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(groupStandards -> {
                    this.groupStandardsIds = extractGroupStandardsIds(groupStandards);
                    getViewState().showGroupStandards(groupStandards);
                });
    }

    Long[] extractGroupStandardsIds(List<GroupStandard> groupStandards) {
        List<Long> groupStandardsIds = new ArrayList<>(groupStandards.size());
        for (GroupStandard groupStandard : groupStandards) {
            groupStandardsIds.add(groupStandard.getId());
        }

        return groupStandardsIds.toArray(new Long[groupStandardsIds.size()]);
    }

}
