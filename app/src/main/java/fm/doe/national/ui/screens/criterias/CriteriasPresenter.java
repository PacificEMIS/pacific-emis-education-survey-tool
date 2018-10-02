package fm.doe.national.ui.screens.criterias;

import android.support.annotation.Nullable;
import android.util.LongSparseArray;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.files.PicturesRepository;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@InjectViewState
public class CriteriasPresenter extends BasePresenter<CriteriasView> {

    private static final int ANSWER_UPDATE_TIMEOUT = 500;
    private final CloudUploader cloudUploader = MicronesiaApplication.getAppComponent().getCloudUploader();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final PicturesRepository picturesRepository = MicronesiaApplication.getAppComponent().getPicturesRepository();

    private final long passingId;
    private final long categoryId;
    private int standardIndex;
    private int nextIndex;
    private int previousIndex;
    private List<Standard> standards = Collections.emptyList();

    @Nullable
    private SubCriteria selectedSubCriteria;

    private LongSparseArray<PublishSubject<SubCriteria>> subCriteriaChangeSubjects = new LongSparseArray<>();

    @Nullable
    private File takenPictureFile;

    public CriteriasPresenter(long passingId, long categoryId, long standardId) {
        this.passingId = passingId;
        this.categoryId = categoryId;
        load(standardId);
    }

    public void onSubCriteriaStateChanged(SubCriteria subCriteria, Answer.State previousState) {
        Answer answer = subCriteria.getAnswer();

        CategoryProgress categoryProgress = standards.get(standardIndex).getCategoryProgress();
        categoryProgress.recalculate(previousState, answer.getState());
        updateProgress();

        subCriteriaChangeSubjects.get(subCriteria.getId()).onNext(subCriteria);
    }

    private void updateProgress() {
        CategoryProgress categoryProgress = standards.get(standardIndex).getCategoryProgress();
        getViewState().setProgress(
                categoryProgress.getAnsweredQuestionsCount(),
                categoryProgress.getTotalQuestionsCount());
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
        previousIndex = getPrevIndex();
        updateUi();
    }

    public void onAddPhotoClicked(SubCriteria subCriteria) {
        selectedSubCriteria = subCriteria;
        try {
            takenPictureFile = picturesRepository.createEmptyFile();
            if (takenPictureFile != null) getViewState().takePictureTo(takenPictureFile);
        } catch (IOException ex) {
            getViewState().showMessage(Text.from(R.string.title_warning), Text.from(R.string.error_take_picture));
        }
    }

    public void onTakePhotoSuccess() {
        if (selectedSubCriteria == null || takenPictureFile == null) return;
        selectedSubCriteria.getAnswer().getPhotos().add(takenPictureFile.getPath());
        takenPictureFile = null;
        afterAnyPhotoChanges(selectedSubCriteria);
    }

    public void onTakePhotoFailure() {
        picturesRepository.delete(takenPictureFile);
        takenPictureFile = null;
        selectedSubCriteria = null; // just silently do nothing
    }

    public void onDeletePhotoClicked(SubCriteria subCriteria, String photoPath) {
        subCriteria.getAnswer().getPhotos().remove(photoPath);
        afterAnyPhotoChanges(subCriteria);
    }

    public void onAddCommentClicked(SubCriteria subCriteria) {
        selectedSubCriteria = subCriteria;
        getViewState().showCommentEditor(subCriteria);
    }

    public void onEditCommentClicked(SubCriteria subCriteria) {
        selectedSubCriteria = subCriteria;
        getViewState().showCommentEditor(subCriteria);
    }

    public void onCommentEdit(String comment) {
        if (selectedSubCriteria == null) return;
        Answer answer = selectedSubCriteria.getAnswer();
        answer.setComment(comment);
        updateAnswer(passingId, selectedSubCriteria.getId(), answer);
        getViewState().notifySubCriteriaChanged(selectedSubCriteria);
        selectedSubCriteria = null;
    }

    public void onDeleteCommentClicked(SubCriteria subCriteria) {
        subCriteria.getAnswer().setComment(null);
        getViewState().notifySubCriteriaChanged(subCriteria);
    }

    public void onMorePhotosClick(SubCriteria subCriteria) {
        selectedSubCriteria = subCriteria;
        getViewState().navigateToPhotos(passingId, subCriteria);
    }

    public void onReturnedFromMorePhotos() {
        if (selectedSubCriteria == null) return;

        addDisposable(dataSource.requestAnswer(passingId, selectedSubCriteria.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(answer -> {
                    selectedSubCriteria.getAnswer().setPhotos(answer.getPhotos());
                    getViewState().notifySubCriteriaChanged(selectedSubCriteria);
                    selectedSubCriteria = null;
                }, this::handleError));
    }

    private void afterAnyPhotoChanges(SubCriteria subCriteria) {
        updateAnswer(passingId, subCriteria.getId(), subCriteria.getAnswer());
        getViewState().notifySubCriteriaChanged(subCriteria);
    }

    private void updateUi() {
        CriteriasView view = getViewState();
        Standard currentStandard = standards.get(standardIndex);
        Standard prevStandard = standards.get(previousIndex);
        Standard nextStandard = standards.get(nextIndex);

        view.setGlobalInfo(currentStandard.getName(), currentStandard.getIndex());
        view.setPrevStandard(prevStandard.getName(), prevStandard.getIndex());
        view.setNextStandard(nextStandard.getName(), nextStandard.getIndex());

        loadQuestions();
    }

    private void loadQuestions() {
        long standardId = standards.get(standardIndex).getId();
        addDisposable(dataSource.requestCriterias(passingId, standardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(criterias -> {
                    getViewState().setCriterias(criterias);
                    initSubCriteriaSubjects(criterias);
                    updateProgress();
                }, this::handleError));
    }

    private void initSubCriteriaSubjects(List<Criteria> criterias) {
        for (Criteria criteria : criterias) {
            for (SubCriteria subCriteria : criteria.getSubCriterias()) {
                PublishSubject<SubCriteria> subject = PublishSubject.create();
                subCriteriaChangeSubjects.append(subCriteria.getId(), subject);

                addDisposable(subject
                        .throttleLast(ANSWER_UPDATE_TIMEOUT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .flatMapSingle(changedSubCriteria -> dataSource.updateAnswer(passingId, changedSubCriteria.getId(), changedSubCriteria.getAnswer()))
                        .subscribe(answer -> cloudUploader.scheduleUploading(passingId), this::handleError));
            }
        }
    }

    private int getNextIndex() {
        return standardIndex < standards.size() - 1 ? standardIndex + 1 : 0;
    }

    private int getPrevIndex() {
        return standardIndex > 0 ? standardIndex - 1 : standards.size() - 1;
    }

    private void initStandardIndexes(Standard standard) {
        standardIndex = standards.indexOf(standard);
        nextIndex = getNextIndex();
        previousIndex = getPrevIndex();
    }

    private void load(long standardId) {
        addDisposable(dataSource.requestStandards(passingId, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .doOnSuccess(standards -> this.standards = standards)
                .flatMap(standards -> dataSource.requestStandard(passingId, standardId))
                .subscribe(standard -> {
                    initStandardIndexes(standard);
                    updateUi();
                }, this::handleError));

        addDisposable(dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .doOnSuccess(passing -> {
                    CriteriasView view = getViewState();
                    view.setSurveyDate(passing.getStartDate());
                    view.setSchoolName(passing.getSchool().getName());
                })
                .flatMap(passing -> dataSource.requestCategories(passingId))
                .toObservable()
                .flatMapIterable(it -> it)
                .filter(category -> category.getId() == categoryId)
                .subscribe(category -> getViewState().setCategoryName(category.getName()), this::handleError));
    }

    private void updateAnswer(long passingId, long subCriteriaId, Answer answer) {
        addDisposable(dataSource.updateAnswer(passingId, subCriteriaId, answer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((updatedAnswer) -> cloudUploader.scheduleUploading(passingId), this::handleError));
    }
}
