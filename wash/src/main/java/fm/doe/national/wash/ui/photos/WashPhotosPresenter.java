package fm.doe.national.wash.ui.photos;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.survey_core.ui.photos.PhotosPresenter;
import fm.doe.national.wash_core.data.model.mutable.MutableAnswer;
import fm.doe.national.wash_core.di.WashCoreComponent;
import fm.doe.national.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WashPhotosPresenter extends PhotosPresenter {

    private final WashSurveyInteractor interactor;

    private MutableAnswer answer;

    public WashPhotosPresenter(CoreComponent coreComponent,
                               RemoteStorageComponent remoteStorageComponent,
                               WashCoreComponent accreditationCoreComponent) {
        super(coreComponent, remoteStorageComponent);
        interactor = accreditationCoreComponent.getWashSurveyInteractor();
        afterInit();
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
