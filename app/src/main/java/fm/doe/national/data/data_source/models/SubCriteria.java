package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface SubCriteria extends Serializable {
    Criteria getCriteria();

    @NonNull
    String getName();
}
