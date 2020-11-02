package org.pacific_emis.surveys.accreditation_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableCriteria;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomCriteria;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomSubCriteria;

public class RelativeRoomCriteria {

    @Embedded
    public RoomCriteria criteria;

    @Relation(parentColumn = "uid", entityColumn = "criteria_id", entity = RoomSubCriteria.class)
    public List<RelativeRoomSubCriteria> subCriterias;

    public MutableCriteria toMutableCriteria() {
        MutableCriteria mutableCriteria = new MutableCriteria(criteria);
        mutableCriteria.setSubCriterias(
                subCriterias.stream().map(RelativeRoomSubCriteria::toMutableSubCriteria).collect(Collectors.toList())
        );
        return mutableCriteria;
    }

}
