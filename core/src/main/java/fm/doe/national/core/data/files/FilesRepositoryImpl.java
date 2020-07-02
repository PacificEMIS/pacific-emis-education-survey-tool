package fm.doe.national.core.data.files;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

import fm.doe.national.core.R;

public class FilesRepositoryImpl implements FilesRepository {
    private static final String EXT_PICTURE = ".jpg";
    private static final String PATTERN_IMAGE_FILENAME = "IMG_%d";
    private final File externalPicturesDirectory;
    private final File cacheDirectory;
    private final Context appContext;

    public FilesRepositoryImpl(Context context) {
        appContext = context;
        externalPicturesDirectory = Environment.getExternalStoragePublicDirectory(appContext.getString(R.string.app_name));
        cacheDirectory = context.getCacheDir();
    }

    @Override
    public File createEmptyImageFile() throws IOException {
        String imageFileName = String.format(Locale.getDefault(), PATTERN_IMAGE_FILENAME, new Date().getTime());
        File file = File.createTempFile(imageFileName, EXT_PICTURE, getExternalDirectory());
        notifyFileSystem();
        return file;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete(File file) {
        file.delete();
    }

    @Override
    @Nullable
    public File createEmptyImageFile(String name) throws IOException {
        File file = new File(getExternalDirectory(), name + EXT_PICTURE);
        boolean isCreated = file.createNewFile();

        if (isCreated) {
            notifyFileSystem();
            return file;
        } else {
            return null;
        }
    }

    private void notifyFileSystem() {
        MediaScannerConnection.scanFile(
                appContext,
                new String[]{getExternalDirectory().getAbsolutePath()},
                null,
                (path, uri) -> {
                    // nothing
                }
        );
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

    public File getExternalDirectory() {
        if (!externalPicturesDirectory.exists()) {
            externalPicturesDirectory.mkdir();
        }
        return externalPicturesDirectory;
    }

    @Override
    public File createFile(String name, String ext) throws IOException {
        // createTempFile will generate unique name
        return File.createTempFile(name, ext, getExternalDirectory());
    }
}
