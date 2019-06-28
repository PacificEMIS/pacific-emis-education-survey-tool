package fm.doe.national.core.data.files;

import java.io.File;
import java.io.IOException;

public interface PicturesRepository {
    File createEmptyFile() throws IOException;

    File createEmptyFile(String name) throws IOException;

    void delete(File file);
}
