package fm.doe.national.ui.screens.settings;

import java.util.List;

import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.ui.screens.base.BaseView;

public interface SettingsView extends BaseView {
    void showAccountConnections(List<CloudAccountData> viewDataList);
    void hideDropboxConnect();
    void hideDriveConnect();
}
