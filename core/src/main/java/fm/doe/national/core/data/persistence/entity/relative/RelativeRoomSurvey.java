package fm.doe.national.core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.mutable.MutableSurvey;
import fm.doe.national.core.data.persistence.entity.RoomCategory;
import fm.doe.national.core.data.persistence.entity.RoomSurvey;

public class RelativeRoomSurvey {

    @Embedded
    public RoomSurvey survey;

    @Relation(parentColumn = "uid", entityColumn = "survey_id", entity = RoomCategory.class)
    public List<RelativeRoomCategory> categories;

    public MutableSurvey toMutableSurvey() {
        MutableSurvey mutableSurvey = new MutableSurvey(survey);
        mutableSurvey.setCategories(categories.stream().map(RelativeRoomCategory::toMutableCategory).collect(Collectors.toList()));
        return mutableSurvey;
    }

}
