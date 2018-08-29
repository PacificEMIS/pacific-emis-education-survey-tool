package fm.doe.national.ui.listeners;

import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;

public interface FilePickerListener {
    void onBrowsingObjectPicked(BrowsingTreeObject object);
}
