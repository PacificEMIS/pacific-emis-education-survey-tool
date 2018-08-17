package fm.doe.national.data.data_source.models;

import java.io.Serializable;

public interface Survey extends Serializable {
    int getVersion();

    String getType();
}
