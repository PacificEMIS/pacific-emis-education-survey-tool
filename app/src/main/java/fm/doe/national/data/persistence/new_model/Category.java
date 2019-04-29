package fm.doe.national.data.persistence.new_model;

import androidx.annotation.NonNull;

import java.util.List;

public interface Category extends IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    List<Standard> getStandards();
}
