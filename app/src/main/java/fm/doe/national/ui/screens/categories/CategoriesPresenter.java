package fm.doe.national.ui.screens.categories;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.custom_views.summary.SummaryViewData;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CategoriesPresenter extends BasePresenter<CategoriesView> {

    private final long passingId;

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    public CategoriesPresenter(long passingId) {
        this.passingId = passingId;
    }

    @Override
    public void attachView(CategoriesView view) {
        super.attachView(view);
        loadPassing();
        loadCategories();
    }

    public void onCategoryClicked(GroupStandard group) {
        getViewState().navigateToStandardsScreen(passingId, group.getId());
    }

    private void loadPassing() {
        addDisposable(dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(passing -> {
                    CategoriesView view = getViewState();
                    view.setSurveyYear(passing.getYear());
                    view.setSchoolName(passing.getSchool().getName());
                }, this::handleError));
    }

    private void loadCategories() {
        addDisposable(dataSource.requestGroupStandards(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::onCategoriesLoaded, this::handleError));
    }

    private void onCategoriesLoaded(List<GroupStandard> categories) {
        getViewState().showGroupStandards(categories);

        addDisposable(requestSummary(categories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showSummaryLoading())
                .doFinally(getViewState()::hideSummaryLoading)
                .subscribe(getViewState()::setSummaryData, this::handleError));
    }

    private Single<List<SummaryViewData>> requestSummary(List<GroupStandard> categories) {
        return Observable.fromIterable(categories)
                .flatMapSingle(category -> dataSource.requestStandards(passingId, category.getId())
                        .flatMap(standards -> Observable.fromIterable(standards)
                                .flatMapSingle(standard -> dataSource.requestCriterias(passingId, standard.getId())
                                        .flatMap(criterias -> Observable.fromIterable(criterias)
                                                .map(criteria -> {
                                                    int positiveAnswersCount = 0;
                                                    for (SubCriteria subCriteria : criteria.getSubCriterias()) {
                                                        if (subCriteria.getAnswer().getState() == Answer.State.POSITIVE) {
                                                            positiveAnswersCount++;
                                                        }
                                                    }
                                                    return new SummaryViewData.Standard.Progress(
                                                            criteria.getCategoryProgress().getTotalQuestionsCount(),
                                                            positiveAnswersCount);
                                                })
                                                .toList())
                                        .map(progresses -> new SummaryViewData.Standard(standard.getName(), progresses)))
                                .toList())
                        .map(standardViewDatas -> new SummaryViewData(category.getName(), standardViewDatas)))
                .toList();
    }

}
