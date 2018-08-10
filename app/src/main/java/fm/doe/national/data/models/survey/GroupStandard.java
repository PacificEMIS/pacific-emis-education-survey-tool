package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import java.util.List;

public interface GroupStandard {
    @NonNull
    String getName();

    List<? extends Standard> getStandards();
}
