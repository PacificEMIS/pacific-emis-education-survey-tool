package fm.doe.national.ui.screens.criterias;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.data.model.Answer;
import fm.doe.national.core.data.model.AnswerState;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.core.data.model.mutable.MutableAnswer;
import fm.doe.national.core.data.model.mutable.MutableCriteria;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.data.model.mutable.MutableStandard;
import fm.doe.national.core.data.model.mutable.MutableSubCriteria;
import fm.doe.national.core.data.model.mutable.MutableSurvey;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CriteriasPresenter extends BasePresenter<CriteriasView> {

    private final CloudUploader cloudUploader = MicronesiaApplication.getInjection().getAppComponent().getCloudUploader();
    private final PicturesRepository picturesRepository = MicronesiaApplication.getInjection().getCoreComponent().getPicturesRepository();
    private final SurveyInteractor surveyInteractor = MicronesiaApplication.getInjection().getCoreComponent().getSurveyInteractor();
    private final long categoryId;

    private int standardIndex;
    private int nextIndex;
    private int previousIndex;
    private List<MutableStandard> standards = Collections.emptyList();

    @Nullable
    private MutableSubCriteria selectedSubCriteria;

    @Nullable
    private File takenPictureFile;

    public CriteriasPresenter(long categoryId, long standardId) {
        this.categoryId = categoryId;
        load(standardId);
        addDisposable(surveyInteractor.getCriteriaProgressSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(criteria -> getViewState().notifyCriteriaChanged(criteria), this::handleError));
    }

    public void onSubCriteriaStateChanged(SubCriteria subCriteria, AnswerState newState) {
        MutableSubCriteria mutableSubCriteria = MutableSubCriteria.toMutable(subCriteria);
        mutableSubCriteria.getAnswer().setState(newState);
        updateAnswer(mutableSubCriteria.getId(), mutableSubCriteria.getAnswer());
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
        selectedSubCriteria = MutableSubCriteria.toMutable(subCriteria);
        try {
            takenPictureFile = picturesRepository.createEmptyFile();
            if (takenPictureFile != null) getViewState().takePictureTo(takenPictureFile);
        } catch (IOException ex) {
            getViewState().showMessage(Text.from(R.string.title_warning), Text.from(R.string.error_take_picture));
        }
    }

    public void onTakePhotoSuccess() {
        if (selectedSubCriteria == null || takenPictureFile == null) return;
        MutablePhoto mutablePhoto = new MutablePhoto();
        mutablePhoto.setLocalPath(takenPictureFile.getPath());
        selectedSubCriteria.getAnswer().getPhotos().add(mutablePhoto);
        takenPictureFile = null;
        afterAnyPhotoChanges(selectedSubCriteria);
    }

    public void onTakePhotoFailure() {
        picturesRepository.delete(takenPictureFile);
        takenPictureFile = null;
        selectedSubCriteria = null; // just silently do nothing
    }

    public void onDeletePhotoClicked(SubCriteria subCriteria, Photo photo) {
        subCriteria.getAnswer().getPhotos().remove(photo);
        afterAnyPhotoChanges(subCriteria);
    }

    public void onAddCommentClicked(SubCriteria subCriteria) {
        selectedSubCriteria = MutableSubCriteria.toMutable(subCriteria);
        getViewState().showCommentEditor(subCriteria);
    }

    public void onEditCommentClicked(SubCriteria subCriteria) {
        selectedSubCriteria = MutableSubCriteria.toMutable(subCriteria);
        getViewState().showCommentEditor(subCriteria);
    }

    public void onCommentEdit(String comment) {
        if (selectedSubCriteria == null) return;
        MutableAnswer answer = selectedSubCriteria.getAnswer();
        answer.setComment(comment);
        updateAnswer(selectedSubCriteria.getId(), answer);
        getViewState().notifySubCriteriaChanged(selectedSubCriteria);
        selectedSubCriteria = null;
    }

    public void onDeleteCommentClicked(SubCriteria subCriteria) {
        MutableSubCriteria mutableSubCriteria = MutableSubCriteria.toMutable(subCriteria);
        mutableSubCriteria.getAnswer().setComment(null);
        updateAnswer(subCriteria.getId(), subCriteria.getAnswer());
    }

    public void onMorePhotosClick(SubCriteria subCriteria) {
        selectedSubCriteria = MutableSubCriteria.toMutable(subCriteria);
        getViewState().navigateToPhotos(categoryId, getStandardId(), getCriteriaId(subCriteria.getId()), subCriteria.getId());
    }

    private void afterAnyPhotoChanges(SubCriteria subCriteria) {
        updateAnswer(subCriteria.getId(), subCriteria.getAnswer());
        getViewState().notifySubCriteriaChanged(subCriteria);
    }

    private void updateUi() {
        CriteriasView view = getViewState();
        Standard currentStandard = standards.get(standardIndex);
        Standard prevStandard = standards.get(previousIndex);
        Standard nextStandard = standards.get(nextIndex);

        view.setGlobalInfo(currentStandard.getTitle(), currentStandard.getSuffix());
        view.setPrevStandard(prevStandard.getTitle(), prevStandard.getSuffix());
        view.setNextStandard(nextStandard.getTitle(), nextStandard.getSuffix());

        loadQuestions();
    }

    private long getStandardId() {
        return standards.get(standardIndex).getId();
    }

    private void loadQuestions() {
        long standardId = getStandardId();

        addDisposable(surveyInteractor.requestCriterias(categoryId, standardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(criterias -> getViewState().setCriterias(new ArrayList<>(criterias)), this::handleError));
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
        addDisposable(surveyInteractor.requestStandards(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .doOnSuccess(standards -> this.standards = standards)
                .flatMapObservable(Observable::fromIterable)
                .filter(standard -> standard.getId() == standardId)
                .firstOrError()
                .subscribe(standard -> {
                    initStandardIndexes(standard);
                    updateUi();
                }, this::handleError));

        MutableSurvey survey = surveyInteractor.getCurrentSurvey();
        getViewState().setSurveyDate(survey.getDate());
        getViewState().setSchoolName(survey.getSchoolName());

        addDisposable(surveyInteractor.requestCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .flatMapObservable(Observable::fromIterable)
                .filter(category -> category.getId() == categoryId)
                .firstOrError()
                .subscribe(category -> getViewState().setCategoryName(category.getTitle()), this::handleError));
    }

    private void updateAnswer(long subCriteriaId, Answer answer) {
        addDisposable(surveyInteractor.updateAnswer(answer, categoryId, getStandardId(), getCriteriaId(subCriteriaId), subCriteriaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> cloudUploader.scheduleUploading(surveyInteractor.getCurrentSurvey().getId()),
                        this::handleError)
        );
    }

    private long getCriteriaId(long subCriteriaId) {
        for (MutableCriteria criteria : standards.get(standardIndex).getCriterias()) {
            for (MutableSubCriteria subCriteria : criteria.getSubCriterias()) {
                if (subCriteria.getId() == subCriteriaId) {
                    return criteria.getId();
                }
            }
        }
        return -1;
    }
}
