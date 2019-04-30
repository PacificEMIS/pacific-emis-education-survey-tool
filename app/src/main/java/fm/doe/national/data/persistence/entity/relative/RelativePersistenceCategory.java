package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.PersistenceStandard;

public class RelativePersistenceCategory {

    @Embedded
    public PersistenceCategory category;

    @Relation(parentColumn = "uid", entityColumn = "category_id", entity = PersistenceStandard.class)
    public List<RelativePersistenceStandard> standards;

}
