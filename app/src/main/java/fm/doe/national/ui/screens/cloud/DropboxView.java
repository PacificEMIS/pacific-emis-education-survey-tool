package fm.doe.national.ui.screens.cloud;

import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.screens.base.BaseView;

public interface DropboxView extends BaseView {
    void startAuthentication();
    void die();
    void showFilePicker(BrowsingTreeObject root);
    void showFolderPicker(BrowsingTreeObject root);

    enum Action {
        AUTH, PICK_FILE, PICK_FOLDER
    }
}
