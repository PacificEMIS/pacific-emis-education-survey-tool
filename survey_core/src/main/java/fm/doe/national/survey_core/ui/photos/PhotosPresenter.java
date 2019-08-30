package fm.doe.national.survey_core.ui.photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.survey_core.R;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class PhotosPresenter<T extends PhotosView> extends BasePresenter<T> {

    private final FilesRepository filesRepository;

    @Nullable
    private File takenPictureFile;

    private Context appContext;

    protected PhotosPresenter(CoreComponent coreComponent) {
        filesRepository = coreComponent.getFilesRepository();
        appContext = coreComponent.getContext();
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

    protected abstract void scheduleUploading(long surveyId);

    public void onDeletePhotoClick(Photo photo) {
        removePhoto(photo);
        onAnswerLoaded();
        update();
    }

    private void update() {
        addDisposable(updateAnswer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> scheduleUploading(getSurveyId()), this::handleError));
    }

    protected void onAnswerLoaded() {
        getViewState().showPhotos(getPhotos());
    }

    public void onAddPhotoPressed() {
        try {
            takenPictureFile = filesRepository.createEmptyImageFile();
            if (takenPictureFile != null) getViewState().takePictureTo(takenPictureFile);
        } catch (IOException ex) {
            getViewState().showMessage(Text.from(R.string.title_warning), Text.from(R.string.error_take_picture));
        }
    }

    public void onTakePhotoSuccess() {
        if (takenPictureFile == null) return;
        addDisposable(
                Completable.fromAction(() -> {
                    compressPhoto(takenPictureFile);
                    MutablePhoto mutablePhoto = new MutablePhoto();
                    mutablePhoto.setLocalPath(takenPictureFile.getPath());
                    addPhoto(mutablePhoto);
                    update();
                    takenPictureFile = null;
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(this::onAnswerLoaded, this::handleError)
        );
    }

    private void compressPhoto(File file) {
        try {
            Bitmap bitmap = ViewUtils.handleSamplingAndRotationBitmap(appContext, Uri.fromFile(file));

            if (bitmap != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.close();
                bitmap.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTakePhotoFailure() {
        filesRepository.delete(takenPictureFile);
        takenPictureFile = null;
    }
}
