package fm.doe.national.core.data.files;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

public class FilesRepositoryImpl implements FilesRepository {
    private static final String EXT_PICTURE = ".jpg";
    private static final String PATTERN_IMAGE_FILENAME = "IMG_%d";
    private final File externalPicturesDirectory;
    private final File cacheDirectory;

    public FilesRepositoryImpl(Context context) {
        externalPicturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        cacheDirectory = context.getCacheDir();
    }

    @Override
    public File createEmptyImageFile() throws IOException {
        String imageFileName = String.format(Locale.getDefault(), PATTERN_IMAGE_FILENAME, new Date().getTime());
        return File.createTempFile(imageFileName, EXT_PICTURE, externalPicturesDirectory);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete(File file) {
        file.delete();
    }

    @Override
    @Nullable
    public File createEmptyImageFile(String name) throws IOException {
        File file = new File(externalPicturesDirectory, name + EXT_PICTURE);
        boolean isCreated = file.createNewFile();
        return isCreated ? file : null;
    }

    @Override
    public File createTmpFile(String name, String ext) throws IOException {
        return File.createTempFile(name, ext, cacheDirectory);
    }

    @Override
    public File createTmpFile(String name, String ext, InputStream inputStream) throws IOException {
        File file = createTmpFile(name, ext);
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int readBytes;
            fos = new FileOutputStream(file);
            while ((readBytes = inputStream.read(data)) > -1) {
                fos.write(data, 0, readBytes);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }
}
