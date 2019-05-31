package fm.doe.national.accreditation.ui.photos;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fm.doe.national.accreditation.R;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.MutableAnswer;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PhotosPresenter extends BasePresenter<PhotosView> {

    private final CloudUploader cloudUploader;
    private final SurveyInteractor interactor;
    private final PicturesRepository picturesRepository;

    private final long categoryId;
    private final long standardId;
    private final long criteriaId;
    private final long subCriteriaId;

    private MutableAnswer answer;

    @Nullable
    private File takenPictureFile;

    PhotosPresenter(CoreComponent coreComponent,
                    CloudComponent cloudComponent,
                    long categoryId,
                    long standardId,
                    long criteriaId,
                    long subCriteriaId) {
        interactor = coreComponent.getSurveyInteractor();
        picturesRepository = coreComponent.getPicturesRepository();
        cloudUploader = cloudComponent.getCloudUploader();
        this.categoryId = categoryId;
        this.standardId = standardId;
        this.criteriaId = criteriaId;
        this.subCriteriaId = subCriteriaId;
        loadAnswer();
    }

    void onDeletePhotoClick(Photo photo) {
        MutablePhoto mutablePhoto = MutablePhoto.toMutable(photo);
        answer.getPhotos().remove(mutablePhoto);
        getViewState().showPhotos(new ArrayList<>(answer.getPhotos()));
        update();
    }

    private void update() {
        addDisposable(interactor.updateAnswer(answer, categoryId, standardId, criteriaId, subCriteriaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(interactor.getCurrentSurvey().getId()), this::handleError));
    }

    private void loadAnswer() {
        addDisposable(interactor.requestCriterias(categoryId, standardId)
                .flatMapObservable(Observable::fromIterable)
                .filter(criteria -> criteria.getId() == criteriaId)
                .firstElement()
                .flatMapObservable(criteria -> Observable.fromIterable(criteria.getSubCriterias()))
                .filter(subCriteria -> subCriteria.getId() == subCriteriaId)
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(getViewState()::hideWaiting)
                .subscribe(subCriteria -> {
                    answer = subCriteria.getAnswer();
                    getViewState().showPhotos(new ArrayList<>(answer.getPhotos()));
                }, this::handleError));
    }

    void onAddPhotoPressed() {
        try {
            takenPictureFile = picturesRepository.createEmptyFile();
            if (takenPictureFile != null) getViewState().takePictureTo(takenPictureFile);
        } catch (IOException ex) {
            getViewState().showMessage(Text.from(R.string.title_warning), Text.from(R.string.error_take_picture));
        }
    }

    void onTakePhotoSuccess() {
        if (takenPictureFile == null) return;
        MutablePhoto mutablePhoto = new MutablePhoto();
        mutablePhoto.setLocalPath(takenPictureFile.getPath());
        answer.getPhotos().add(mutablePhoto);
        takenPictureFile = null;
        getViewState().showPhotos(new ArrayList<>(answer.getPhotos()));
        update();
    }

    void onTakePhotoFailure() {
        picturesRepository.delete(takenPictureFile);
        takenPictureFile = null;
    }
}
