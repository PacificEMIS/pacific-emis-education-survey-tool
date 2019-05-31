package fm.doe.national.cloud.di;

import dagger.Component;
import fm.doe.national.cloud.model.CloudRepository;
import fm.doe.national.cloud.model.drive.DriveCloudAccessor;
import fm.doe.national.cloud.model.dropbox.DropboxCloudAccessor;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.di.CoreComponent;

@CloudScope
@Component(modules = {
        CloudModule.class
}, dependencies = {
        CoreComponent.class
})
public interface CloudComponent {

    CloudRepository getCloudRepository();

    CloudUploader getCloudUploader();

    DriveCloudAccessor getDriveCloudAccessor();

    DropboxCloudAccessor getDropboxCloudAccessor();

}
