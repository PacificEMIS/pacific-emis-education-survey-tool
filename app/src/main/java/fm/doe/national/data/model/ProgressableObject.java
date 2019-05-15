package fm.doe.national.data.model;

import androidx.annotation.NonNull;

public interface ProgressableObject extends IdentifiedObject {

    @NonNull
    Progress getProgress();

}
