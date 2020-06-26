package org.pacific_emis.surveys.wash_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableSubGroup;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomQuestion;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomSubGroup;

public class RelativeRoomSubGroup {

    @Embedded
    public RoomSubGroup subGroup;

    @Relation(parentColumn = "uid", entityColumn = "sub_group_id", entity = RoomQuestion.class)
    public List<RelativeRoomQuestion> questions;

    public MutableSubGroup toMutableStandard() {
        MutableSubGroup mutableSubGroup = new MutableSubGroup(subGroup);
        mutableSubGroup.setQuestions(questions.stream().map(RelativeRoomQuestion::toMutable).collect(Collectors.toList()));
        return mutableSubGroup;
    }

}
