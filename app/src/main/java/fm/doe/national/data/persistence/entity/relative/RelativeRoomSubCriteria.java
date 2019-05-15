package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomAnswer;
import fm.doe.national.data.persistence.entity.RoomSubCriteria;

public class RelativeRoomSubCriteria {

    @Embedded
    public RoomSubCriteria subCriteria;

    @Relation(parentColumn = "uid", entityColumn = "sub_criteria_id", entity = RoomAnswer.class)
    public List<RelativeRoomAnswer> answers;

}
