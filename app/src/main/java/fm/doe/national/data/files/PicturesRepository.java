package fm.doe.national.data.files;

import java.io.File;
import java.io.IOException;

public interface PicturesRepository {
    File createEmptyFile() throws IOException;
    void delete(File file);
}
