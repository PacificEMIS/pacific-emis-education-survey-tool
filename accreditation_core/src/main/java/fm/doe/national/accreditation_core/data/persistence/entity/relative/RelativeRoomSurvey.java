package fm.doe.national.accreditation_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomCategory;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomAccreditationSurvey;

public class RelativeRoomSurvey {

    @Embedded
    public RoomAccreditationSurvey survey;

    @Relation(parentColumn = "uid", entityColumn = "survey_id", entity = RoomCategory.class)
    public List<RelativeRoomCategory> categories;

    public MutableAccreditationSurvey toMutableSurvey() {
        MutableAccreditationSurvey mutableSurvey = new MutableAccreditationSurvey(survey);
        mutableSurvey.setCategories(categories.stream().map(RelativeRoomCategory::toMutableCategory).collect(Collectors.toList()));
        return mutableSurvey;
    }

}
