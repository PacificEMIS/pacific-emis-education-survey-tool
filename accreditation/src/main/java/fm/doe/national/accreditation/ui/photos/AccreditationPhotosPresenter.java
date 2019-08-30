package fm.doe.national.accreditation.ui.photos;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.accreditation_core.data.model.mutable.MutableAnswer;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.survey_core.ui.photos.PhotosPresenter;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationPhotosPresenter extends PhotosPresenter<AccreditationPhotosView> {

    private final AccreditationSurveyInteractor interactor;
    private final RemoteStorageAccessor remoteStorageAccessor;

    private MutableAnswer answer;

    public AccreditationPhotosPresenter(CoreComponent coreComponent,
                                        RemoteStorageComponent remoteStorageComponent,
                                        AccreditationCoreComponent accreditationCoreComponent) {
        super(coreComponent);
        interactor = accreditationCoreComponent.getAccreditationSurveyInteractor();
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
        answer.getPhotos().remove(mutablePhoto);
    }

    @Override
    protected Completable updateAnswer() {
        return interactor.updateAnswer(answer);
    }

    @Override
    protected void loadAnswer() {
        addDisposable(interactor.requestCriterias(interactor.getCurrentCategoryId(), interactor.getCurrentStandardId())
                .flatMapObservable(Observable::fromIterable)
                .filter(criteria -> criteria.getId() == interactor.getCurrentCriteriaId())
                .firstElement()
                .flatMapObservable(criteria -> Observable.fromIterable(criteria.getSubCriterias()))
                .filter(subCriteria -> subCriteria.getId() == interactor.getCurrentSubCriteriaId())
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(getViewState()::hideWaiting)
                .subscribe(subCriteria -> {
                    answer = subCriteria.getAnswer();
                    onAnswerLoaded();
                }, this::handleError));
    }

    @Override
    protected void addPhoto(MutablePhoto photo) {
        answer.getPhotos().add(photo);
    }

    @Override
    protected List<Photo> getPhotos() {
        return new ArrayList<>(answer.getPhotos());
    }

    @Override
    protected long getSurveyId() {
        return interactor.getCurrentSurvey().getId();
    }
}
