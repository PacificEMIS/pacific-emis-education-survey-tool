package fm.doe.national.ui.screens.categories;

import com.omegar.mvp.InjectViewState;

import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Category;
import fm.doe.national.ui.screens.base.BasePresenter;

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

    public void onCategoryClicked(Category category) {
        getViewState().navigateToStandardsScreen(passingId, category.getId());
    }

    private void loadPassing() {
        // TODO: fixme
//        addDisposable(dataSource.requestSchoolAccreditationPassing(passingId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> getViewState().showWaiting())
//                .doFinally(() -> getViewState().hideWaiting())
//                .subscribe(passing -> {
//                    CategoriesView view = getViewState();
//                    view.setSurveyDate(passing.getStartDate());
//                    view.setSchoolName(passing.getSchool().getName());
//                }, this::handleError));
    }

    private void loadCategories() {
        // TODO: fixme
//        addDisposable(dataSource.requestCategories(passingId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> getViewState().showWaiting())
//                .doFinally(() -> getViewState().hideWaiting())
//                .subscribe(this::onCategoriesLoaded, this::handleError));
    }

    private void onCategoriesLoaded(List<Category> categories) {
        getViewState().showCategories(categories);

        // TODO: fixme
//        addDisposable(requestSummary(categories)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> getViewState().showSummaryLoading())
//                .doFinally(getViewState()::hideSummaryLoading)
//                .subscribe(getViewState()::setSummaryData, this::handleError));
    }

    // TODO: fixme
//    private Single<List<SummaryViewData>> requestSummary(List<Category> categories) {
//        return dataSource.requestStandards(passingId)
//                .flatMap(standards -> Observable.fromIterable(standards)
//                        .flatMapSingle(standard -> Single.zip(
//                                dataSource.requestCategoryOfStandard(passingId, standard),
//                                dataSource.requestCriterias(passingId, standard.getId())
//                                        .flatMap(criterias -> Observable.fromIterable(criterias)
//                                                .map(criteria -> {
//                                                    int positiveAnswersCount = 0;
//                                                    int negativeAnswersCount = 0;
//                                                    for (SubCriteria subCriteria : criteria.getSubCriterias()) {
//                                                        if (subCriteria.getAnswer().getState() == Answer.State.POSITIVE) {
//                                                            positiveAnswersCount++;
//                                                        } else if (subCriteria.getAnswer().getState() == Answer.State.NEGATIVE) {
//                                                            negativeAnswersCount++;
//                                                        }
//                                                    }
//                                                    return new SummaryViewData.Progress(
//                                                            criteria.getCategoryProgress().getTotalQuestionsCount(),
//                                                            positiveAnswersCount,
//                                                            negativeAnswersCount);
//                                                })
//                                                .toList()),
//                                (category, progresses) -> new SummaryViewData(category, standard, progresses)))
//                        .toList());
//    }

}
