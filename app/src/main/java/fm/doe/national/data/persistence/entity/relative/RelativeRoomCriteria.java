package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomCriteria;
import fm.doe.national.data.persistence.entity.RoomSubCriteria;

public class RelativeRoomCriteria {

    @Embedded
    public RoomCriteria criteria;

    @Relation(parentColumn = "uid", entityColumn = "criteria_id", entity = RoomSubCriteria.class)
    public List<RelativeRoomSubCriteria> subCriterias;

}
