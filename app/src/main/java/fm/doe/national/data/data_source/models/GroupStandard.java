package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public interface GroupStandard extends Serializable {
    @NonNull
    String getName();

    List<? extends Standard> getStandards();
}
