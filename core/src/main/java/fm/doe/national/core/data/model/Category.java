package fm.doe.national.core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface Category extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @Nullable
    List<? extends Standard> getStandards();

    EvaluationForm getEvaluationForm();
}
