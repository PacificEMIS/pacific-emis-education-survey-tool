package fm.doe.national.data.files;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FilePicturesRepository implements PicturesRepository {
    private final File externalPicturesDirectory;

    public FilePicturesRepository(Context context) {
        externalPicturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    @Override
    public File createEmptyFile() throws IOException {
        String imageFileName = "IMG_" + String.valueOf(new Date().getTime());
        return File.createTempFile(imageFileName, ".jpg", externalPicturesDirectory);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete(File file) {
        file.delete();
    }
}
