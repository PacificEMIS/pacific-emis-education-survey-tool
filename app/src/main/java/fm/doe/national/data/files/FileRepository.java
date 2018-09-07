package fm.doe.national.data.files;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileRepository {
    private final File externalPicturesDirectory;

    public FileRepository(Context context) {
        externalPicturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public File createEmptyPictureFile() throws IOException {
        String imageFileName = "IMG_" + String.valueOf(new Date().getTime());
        return File.createTempFile(imageFileName, ".jpg", externalPicturesDirectory);
    }

    public void deleteFile(File file) {
        file.delete();
    }
}
