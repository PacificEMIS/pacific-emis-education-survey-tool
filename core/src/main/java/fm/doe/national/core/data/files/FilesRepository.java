package fm.doe.national.core.data.files;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FilesRepository {
    File createEmptyImageFile() throws IOException;

    @Nullable
    File createEmptyImageFile(String name) throws IOException;

    void delete(File file);

    File createTmpFile(String name, String ext) throws IOException;

    File createTmpFile(String name, String ext, InputStream inputStream) throws IOException;

}
