package fm.doe.national.remote_storage.ui.remote_storage;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.core.utils.ObjectUtils;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.remote_storage.utils.SurveyTextUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DriveStoragePresenter extends BasePresenter<DriveStorageView> {

    private final RemoteStorage storage;
    private final RemoteStorageAccessor accessor;
    private final LocalSettings localSettings;
    private final FilesRepository filesRepository;
    private final Stack<GoogleDriveFileHolder> parentsStack = new Stack<>();
    private final boolean isDebugViewer;

    public DriveStoragePresenter(RemoteStorageComponent component, CoreComponent coreComponent, boolean isDebugViewer) {
        this.isDebugViewer = isDebugViewer;
        this.storage = component.getRemoteStorage();
        this.accessor = component.getRemoteStorageAccessor();
        this.localSettings = coreComponent.getLocalSettings();
        this.filesRepository = coreComponent.getFilesRepository();
        updateFileHolders();
    }

    public void onItemPressed(GoogleDriveFileHolder item) {
        switch (item.getMimeType()) {
            case FOLDER:
                parentsStack.push(item);
                updateFileHolders();
                break;
            case FILE:
            case XML:
                requestContent(item);
                break;
            case EXCEL:
            case GOOGLE_SHEETS:
                downloadContent(item);
                break;
        }
    }

    private void downloadContent(GoogleDriveFileHolder item) {
        if (!isDebugViewer) {
            return;
        }

        try {
            File downloadFile = filesRepository.createFile(item.getName(), "." + BuildConfig.EXTENSION_REPORT_TEMPLATE);

            if (!downloadFile.exists()) {
                getViewState().showToast(Text.from(R.string.error_file_not_created));
                return;
            }

            addDisposable(
                    storage.downloadContent(item.getId(), downloadFile, DriveType.EXCEL)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(d -> getViewState().showWaiting())
                            .doFinally(getViewState()::hideWaiting)
                            .subscribe(
                                    () -> getViewState().showToast(
                                            Text.from(R.string.format_file_downloaded, downloadFile.getAbsolutePath())
                                    ),
                                    this::handleError
                            )
            );
        } catch (IOException ex) {
            ex.printStackTrace();
            String errorMessage = ex.getLocalizedMessage();
            Text errorText = null;
            if (errorMessage != null) {
                errorText = Text.from(errorMessage);
            }
            getViewState().showToast(ObjectUtils.orElse(errorText, Text.from(R.string.error_file_not_created)));
        }
    }

    private void updateFileHolders() {
        getViewState().setParentName(getCurrentParentName());
        addDisposable(
                storage.requestStorageFiles(getCurrentParentId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(items -> {
                            String currentSurveyPrefix = SurveyTextUtil.convertSurveyTypeToExportPrefix(
                                    localSettings.getSurveyTypeOrDefault()
                            );
                            List<GoogleDriveFileHolder> itemsToShow = items.stream()
                                    .filter(f -> isDebugViewer || f.getMimeType() == DriveType.FOLDER ||
                                            (f.getMimeType() == DriveType.XML && f.getName().startsWith(currentSurveyPrefix)))
                                    .sorted((lv, rv) -> lv.getMimeType().compareTo(rv.getMimeType()))
                                    .collect(Collectors.toList());

                            getViewState().setItems(itemsToShow);
                        }, this::handleError)
        );
    }

    @Nullable
    private String getCurrentParentId() {
        return parentsStack.isEmpty() ? null : parentsStack.peek().getId();
    }

    @Nullable
    private String getCurrentParentName() {
        return parentsStack.isEmpty() ? null : parentsStack.peek().getName();
    }

    private void requestContent(GoogleDriveFileHolder file) {
        addDisposable(
                storage.loadContent(file.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(content -> {
                            if (isDebugViewer) {
                                getViewState().setContent(file.getSurveyMetadata().toString() + content);
                            } else {
                                accessor.onContentReceived(content);
                                getViewState().close();
                            }
                        }, this::handleError)
        );
    }

    public void onBackPressed() {
        if (parentsStack.isEmpty()) {
            accessor.onContentNotReceived();
            getViewState().close();
        } else {
            parentsStack.pop();
            updateFileHolders();
        }
    }

    public void onItemLongPressed(GoogleDriveFileHolder item) {
        addDisposable(
                storage.delete(item.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(this::updateFileHolders, this::handleError)
        );
    }
}
