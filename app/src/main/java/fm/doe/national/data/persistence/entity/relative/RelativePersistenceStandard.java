package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.PersistenceStandard;

public class RelativePersistenceStandard {

    @Embedded
    public PersistenceStandard standard;

    @Relation(parentColumn = "uid", entityColumn = "standard_id", entity = PersistenceCriteria.class)
    public List<RelativePersistenceCriteria> criterias;

}
