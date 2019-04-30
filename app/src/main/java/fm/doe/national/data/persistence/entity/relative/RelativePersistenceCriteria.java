package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;

public class RelativePersistenceCriteria {

    @Embedded
    public PersistenceCriteria criteria;

    @Relation(parentColumn = "uid", entityColumn = "criteria_id", entity = PersistenceSubCriteria.class)
    public List<RelativePersistenceSubCriteria> subCriterias;

}
