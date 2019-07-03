package fm.doe.national.wash_core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.Survey;

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
}
