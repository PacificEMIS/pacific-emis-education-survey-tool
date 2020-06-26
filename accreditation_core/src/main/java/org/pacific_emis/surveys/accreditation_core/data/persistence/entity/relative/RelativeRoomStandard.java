package org.pacific_emis.surveys.accreditation_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableStandard;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomStandard;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomCriteria;

public class RelativeRoomStandard {

    @Embedded
    public RoomStandard standard;

    @Relation(parentColumn = "uid", entityColumn = "standard_id", entity = RoomCriteria.class)
    public List<RelativeRoomCriteria> criterias;

    public MutableStandard toMutableStandard() {
        MutableStandard mutableStandard = new MutableStandard(standard);
        mutableStandard.setCriterias(criterias.stream().map(RelativeRoomCriteria::toMutableCriteria).collect(Collectors.toList()));
        return mutableStandard;
    }

}
