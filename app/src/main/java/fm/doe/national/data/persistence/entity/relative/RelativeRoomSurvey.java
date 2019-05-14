package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomCategory;
import fm.doe.national.data.persistence.entity.RoomSurvey;

public class RelativeRoomSurvey {

    @Embedded
    public RoomSurvey survey;

    @Relation(parentColumn = "uid", entityColumn = "survey_id", entity = RoomCategory.class)
    public List<RelativeRoomCategory> categories;

}
