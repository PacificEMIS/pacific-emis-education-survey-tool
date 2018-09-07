package fm.doe.national.ui.screens.standard;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.files.FileRepository;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    private final CloudUploader cloudUploader = MicronesiaApplication.getAppComponent().getCloudUploader();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final FileRepository fileRepository = MicronesiaApplication.getAppComponent().getFileRepository();

    private long passingId;
    private int standardIndex;
    private int nextIndex;
    private int previousIndex;
    private List<Standard> standards = new ArrayList<>();

    private SubCriteria subCriteriaOnAction = null;
    private File takenPictureFile = null;

    public StandardPresenter(long passingId, long standardId) {
        this.passingId = passingId;
        load(standardId);
    }

    public void onSubCriteriaStateChanged(SubCriteria subCriteria, Answer.State previousState) {
        Answer answer = subCriteria.getAnswer();
        Answer.State state = answer.getState();

        updateAnswer(passingId, subCriteria.getId(), state, answer.getComment(), answer.getPhotosPaths());

        CategoryProgress categoryProgress = standards.get(standardIndex).getCategoryProgress();
        categoryProgress.recalculate(previousState, state);
        updateProgress();
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
        nextIndex = getPrevIndex();
        updateUi();
    }

    public void onCommentEdit(SubCriteria subCriteria, String comment) {
        Answer answer = subCriteria.getAnswer();
        updateAnswer(passingId, subCriteria.getId(), answer.getState(), comment, answer.getPhotosPaths());
    }

    public void onAddPhotoClicked(SubCriteria subCriteria) {
        subCriteriaOnAction = subCriteria;
        try {
            takenPictureFile = fileRepository.createEmptyPictureFile();
            getViewState().dispatchTakePictureIntent(takenPictureFile);
        } catch (IOException ex) {
            getViewState().showWarning(Text.from(R.string.title_warning), Text.from(R.string.error_take_picture));
        }
    }

    public void onTakePhotoSuccess() {
        subCriteriaOnAction.getAnswer().getPhotosPaths().add(takenPictureFile.getPath());
        takenPictureFile = null;
        afterAnyPhotoChanges(subCriteriaOnAction);
    }

    public void onTakePhotoFailure() {
        fileRepository.deleteFile(takenPictureFile);
        takenPictureFile = null;
        subCriteriaOnAction = null; // just silently do nothing
    }

    public void onDeletePhotoClicked(SubCriteria subCriteria, String photoPath) {
        subCriteria.getAnswer().getPhotosPaths().remove(photoPath);
        afterAnyPhotoChanges(subCriteria);
    }

    private void afterAnyPhotoChanges(SubCriteria subCriteria) {
        Answer answer = subCriteria.getAnswer();
        updateAnswer(passingId, subCriteria.getId(), answer.getState(), answer.getComment(), answer.getPhotosPaths());
        // TODO update UI
    }

    private void updateUi() {
        StandardView view = getViewState();
        view.setGlobalInfo(standards.get(standardIndex).getName(), standardIndex);
        view.setPrevStandard(standards.get(previousIndex).getName(), previousIndex);
        view.setNextStandard(standards.get(nextIndex).getName(), nextIndex);

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
                    updateProgress();
                }, this::handleError));
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
        addDisposable(dataSource.requestStandards(passingId)
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
                .subscribe(passing -> {
                    StandardView view = getViewState();
                    view.setSurveyYear(passing.getYear());
                    view.setSchoolName(passing.getSchool().getName());
                }, this::handleError));
    }

    private void updateAnswer(long passingId, long subCriteriaId, Answer.State state, String comment, List<String> photos) {
        addDisposable(dataSource.updateAnswer(passingId, subCriteriaId, state, comment, photos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(passingId), this::handleError));
    }
}
