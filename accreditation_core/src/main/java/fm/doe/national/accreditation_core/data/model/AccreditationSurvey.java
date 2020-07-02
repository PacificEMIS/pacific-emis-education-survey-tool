package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.Survey;

public interface AccreditationSurvey extends Survey {

    @Nullable
    List<? extends Category> getCategories();

    default int getPhotosCount() {
        return (int) getCategories().parallelStream()
                .flatMap(c -> c.getStandards().parallelStream())
                .flatMap(s -> s.getCriterias().parallelStream())
                .flatMap(c -> c.getSubCriterias().parallelStream())
                .flatMap(sc -> sc.getAnswer().getPhotos().parallelStream())
                .count();
    }

}
