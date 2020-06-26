package org.pacific_emis.surveys.wash.ui.photos;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.mutable.MutablePhoto;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.survey_core.ui.photos.PhotosPresenter;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WashPhotosPresenter extends PhotosPresenter<WashPhotosView> {

    private final WashSurveyInteractor interactor;
    private final RemoteStorageAccessor remoteStorageAccessor;

    private MutableAnswer answer;

    public WashPhotosPresenter(CoreComponent coreComponent,
                               RemoteStorageComponent remoteStorageComponent,
                               WashCoreComponent accreditationCoreComponent) {
        super(coreComponent);
        interactor = accreditationCoreComponent.getWashSurveyInteractor();
        remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();
        afterInit();
    }

    @Override
    protected void scheduleUploading(long surveyId) {
        remoteStorageAccessor.scheduleUploading(surveyId);
    }

    @Override
    protected void removePhoto(Photo photo) {
        MutablePhoto mutablePhoto = MutablePhoto.toMutable(photo);
        if (answer.getPhotos() != null) {
            answer.getPhotos().remove(mutablePhoto);
        }
    }

    @Override
    protected Completable updateAnswer() {
        return interactor.updateAnswer(answer);
    }

    @Override
    protected void loadAnswer() {
        addDisposable(interactor.requestQuestions(interactor.getCurrentGroupId(), interactor.getCurrentSubGroupId())
                .flatMapObservable(Observable::fromIterable)
                .filter(question -> question.getId() == interactor.getCurrentQuestionId())
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(getViewState()::hideWaiting)
                .subscribe(question -> {
                    answer = question.getAnswer();
                    onAnswerLoaded();
                }, this::handleError));
    }

    @Override
    protected void addPhoto(MutablePhoto photo) {
        List<MutablePhoto> photos = answer.getPhotos() == null ? CollectionUtils.emptyArrayList() : answer.getPhotos();
        photos.add(photo);
        answer.setPhotos(photos);
    }

    @Override
    protected List<Photo> getPhotos() {
        return answer.getPhotos() == null ? CollectionUtils.emptyArrayList() : new ArrayList<>(answer.getPhotos());
    }

    @Override
    protected long getSurveyId() {
        return interactor.getCurrentSurvey().getId();
    }
}
