package org.pacific_emis.surveys.accreditation.ui.photos;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.mutable.MutablePhoto;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.survey_core.ui.photos.PhotosPresenter;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationPhotosPresenter extends PhotosPresenter<AccreditationPhotosView> {

    private final AccreditationSurveyInteractor interactor;
    private final AccreditationDataSource dataSource;
    private final RemoteStorageAccessor remoteStorageAccessor;

    private MutableAnswer answer;

    public AccreditationPhotosPresenter(CoreComponent coreComponent,
                                        RemoteStorageComponent remoteStorageComponent,
                                        AccreditationCoreComponent accreditationCoreComponent) {
        super(coreComponent);
        interactor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        dataSource = accreditationCoreComponent.getDataSource();
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
        dataSource.setSurveyUploadState(interactor.getCurrentSurvey(), UploadState.NOT_UPLOAD);
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
