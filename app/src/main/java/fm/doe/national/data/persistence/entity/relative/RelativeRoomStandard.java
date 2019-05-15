package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomCriteria;
import fm.doe.national.data.persistence.entity.RoomStandard;

public class RelativeRoomStandard {

    @Embedded
    public RoomStandard standard;

    @Relation(parentColumn = "uid", entityColumn = "standard_id", entity = RoomCriteria.class)
    public List<RelativeRoomCriteria> criterias;

}
