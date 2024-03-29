package org.pacific_emis.surveys.accreditation_core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.core.data.model.Survey;

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

    @Override
    default MutableAccreditationSurvey toMutable() {
        return new MutableAccreditationSurvey(this);
    }
}
