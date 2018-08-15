package fm.doe.national.data.data_source.models.survey;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collection;

public interface GroupStandard extends Serializable {
    @NonNull
    String getName();

    Collection<? extends Standard> getStandards();
}
