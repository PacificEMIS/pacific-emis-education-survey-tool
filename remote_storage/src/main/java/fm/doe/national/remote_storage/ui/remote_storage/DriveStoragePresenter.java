package fm.doe.national.remote_storage.ui.remote_storage;

import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DriveStoragePresenter extends BasePresenter<DriveStorageView> {

    private final RemoteStorage storage;
    private final Stack<String> parentsStack = new Stack<>();

    public DriveStoragePresenter(RemoteStorageComponent component) {
        storage = component.getRemoteStorage();
        updateFileHolders();
    }

    public void onItemPressed(GoogleDriveFileHolder item) {
        String itemId = item.getId();

        if (itemId == null) {
            parentsStack.pop();
            updateFileHolders();
            return;
        }

        switch (item.getMimeType()) {
            case FOLDER:
                parentsStack.push(itemId);
                updateFileHolders();
                break;
            case FILE:
            case PLAIN_TEXT:
                requestContent(itemId);
                break;
        }
    }

    private void updateFileHolders() {
        addDisposable(
                storage.requestStorageFiles(getCurrentParent())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(items -> {
                            List<GoogleDriveFileHolder> itemsToShow = new ArrayList<>();

                            if (getCurrentParent() != null) {
                                itemsToShow.add(new GoogleDriveFileHolder());
                            }

                            itemsToShow.addAll(
                                    items.stream()
                                            .filter(f -> f.getMimeType() != DriveType.OTHER)
                                            .sorted((lv, rv) -> lv.getMimeType().compareTo(rv.getMimeType()))
                                            .collect(Collectors.toList())
                            );

                            getViewState().setItems(itemsToShow);
                        }, this::handleError)
        );
    }

    @Nullable
    private String getCurrentParent() {
        return parentsStack.isEmpty() ? null : parentsStack.peek();
    }

    private void requestContent(String fileId) {
        addDisposable(
                storage.loadContent(fileId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(getViewState()::setContent, this::handleError)
        );
    }
}
