package fm.doe.national.wash_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;
import fm.doe.national.wash_core.data.persistence.entity.RoomGroup;
import fm.doe.national.wash_core.data.persistence.entity.RoomWashSurvey;

public class RelativeRoomSurvey {

    @Embedded
    public RoomWashSurvey survey;

    @Relation(parentColumn = "uid", entityColumn = "survey_id", entity = RoomGroup.class)
    public List<RelativeRoomGroup> groups;

    public MutableWashSurvey toMutable() {
        MutableWashSurvey mutableSurvey = new MutableWashSurvey(survey);
        mutableSurvey.setGroups(groups.stream().map(RelativeRoomGroup::toMutable).collect(Collectors.toList()));
        return mutableSurvey;
    }

}
