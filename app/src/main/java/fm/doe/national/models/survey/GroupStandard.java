package fm.doe.national.models.survey;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public interface GroupStandard extends Serializable {

    long getId();

    @Nullable
    String getName();

}
