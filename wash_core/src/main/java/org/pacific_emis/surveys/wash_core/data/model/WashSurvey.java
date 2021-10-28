package org.pacific_emis.surveys.wash_core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableWashSurvey;

public interface WashSurvey extends Survey {

    @Nullable
    List<? extends Group> getGroups();

    default int getPhotosCount() {
        return (int) getGroups().parallelStream()
                .flatMap(g -> g.getSubGroups().parallelStream())
                .flatMap(sg -> sg.getQuestions().parallelStream())
                .flatMap(q -> q.getAnswer().getPhotos().parallelStream())
                .count();
    }

    @Override
    default MutableWashSurvey toMutable() {
        return new MutableWashSurvey(this);
    }
}
