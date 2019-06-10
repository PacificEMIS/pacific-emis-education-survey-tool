package fm.doe.national.survey_core.ui.photos;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.survey_core.R;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public abstract class PhotosPresenter extends BasePresenter<PhotosView> {

    private final CloudUploader cloudUploader;
    private final PicturesRepository picturesRepository;

    @Nullable
    private File takenPictureFile;

    protected PhotosPresenter(CoreComponent coreComponent,
                    CloudComponent cloudComponent) {
        picturesRepository = coreComponent.getPicturesRepository();
        cloudUploader = cloudComponent.getCloudUploader();
    }

    // Call this in subclass constructor
    protected void afterInit() {
        loadAnswer();
    }

    protected abstract void removePhoto(Photo photo);
    protected abstract List<Photo> getPhotos();
    protected abstract Completable updateAnswer();
    protected abstract long getSurveyId();
    protected abstract void loadAnswer();
    protected abstract void addPhoto(MutablePhoto photo);

    public void onDeletePhotoClick(Photo photo) {
        removePhoto(photo);
        onAnswerLoaded();
        update();
    }

    private void update() {
        addDisposable(updateAnswer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(getSurveyId()), this::handleError));
    }

    protected void onAnswerLoaded() {
        getViewState().showPhotos(getPhotos());
    }

    public void onAddPhotoPressed() {
        try {
            takenPictureFile = picturesRepository.createEmptyFile();
            if (takenPictureFile != null) getViewState().takePictureTo(takenPictureFile);
        } catch (IOException ex) {
            getViewState().showMessage(Text.from(R.string.title_warning), Text.from(R.string.error_take_picture));
        }
    }

    public void onTakePhotoSuccess() {
        if (takenPictureFile == null) return;
        MutablePhoto mutablePhoto = new MutablePhoto();
        mutablePhoto.setLocalPath(takenPictureFile.getPath());
        addPhoto(mutablePhoto);
        update();
        takenPictureFile = null;
        onAnswerLoaded();
    }

    public void onTakePhotoFailure() {
        picturesRepository.delete(takenPictureFile);
        takenPictureFile = null;
    }
}
