package fm.doe.national.remote_storage.ui.remote_storage;

import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.core.utils.TextUtil;
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
    private final GlobalPreferences globalPreferences;
    private final Stack<GoogleDriveFileHolder> parentsStack = new Stack<>();
    private final boolean isDebugViewer;

    public DriveStoragePresenter(RemoteStorageComponent component, CoreComponent coreComponent, boolean isDebugViewer) {
        this.isDebugViewer = isDebugViewer;
        this.storage = component.getRemoteStorage();
        this.accessor = component.getRemoteStorageAccessor();
        this.globalPreferences = coreComponent.getGlobalPreferences();
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
                                    globalPreferences.getSurveyTypeOrDefault()
                            );
                            List<GoogleDriveFileHolder> itemsToShow = items.stream()
                                    .filter(f -> isDebugViewer || f.getMimeType() == DriveType.FOLDER ||
                                            (f.getMimeType() == DriveType.XML &&
                                                    TextUtil.startsWith(f.getName(), currentSurveyPrefix)))
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
                                getViewState().setContent(file.getNdoeMetadata().toString() + content);
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
