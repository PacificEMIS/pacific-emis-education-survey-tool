package fm.doe.national.ui.screens.cloud;

import fm.doe.national.ui.screens.base.BaseView;

public interface DropboxView extends BaseView {
    void startAuthentication();
    void showSdkChooser();
    void die();

    enum Action {
        AUTH, PICK_FILE, UPLOAD_FILE
    }
}
