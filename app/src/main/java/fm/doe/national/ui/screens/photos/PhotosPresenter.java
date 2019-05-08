package fm.doe.national.ui.screens.photos;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Answer;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PhotosPresenter extends BasePresenter<PhotosView> {

    private final CloudUploader cloudUploader = MicronesiaApplication.getAppComponent().getCloudUploader();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    private final long subCriteriaId;

    private Answer answer;

    public PhotosPresenter(long subCriteriaId) {
        this.subCriteriaId = subCriteriaId;
        loadAnswer();
    }

    public void onDeletePhotoClick(String photo) {
        // TODO: fixme
//        answer.getPhotos().remove(photo);
//        getViewState().showPhotos(answer.getPhotos());
        addDisposable(dataSource.updateAnswer(answer, subCriteriaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((answer) -> cloudUploader.scheduleUploading(interactor.getCurrentSurvey().getId()), this::handleError));
    }

    private void loadAnswer() {
        // TODO: fixme
//        addDisposable(dataSource.requestAnswer(passingId, subCriteriaId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> getViewState().showWaiting())
//                .doFinally(getViewState()::hideWaiting)
//                .subscribe(answerResult -> {
//                    answer = answerResult;
//                    getViewState().showPhotos(answer.getPhotos());
//                }, this::handleError));
    }
}
