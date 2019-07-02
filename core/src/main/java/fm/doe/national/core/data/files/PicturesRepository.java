package fm.doe.national.core.data.files;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;

public interface PicturesRepository {
    File createEmptyFile() throws IOException;

    @Nullable
    File createEmptyFile(String name) throws IOException;

    void delete(File file);
}
