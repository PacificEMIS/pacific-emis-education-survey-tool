package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomCategory;
import fm.doe.national.data.persistence.entity.RoomStandard;

public class RelativeRoomCategory {

    @Embedded
    public RoomCategory category;

    @Relation(parentColumn = "uid", entityColumn = "category_id", entity = RoomStandard.class)
    public List<RelativeRoomStandard> standards;

}
