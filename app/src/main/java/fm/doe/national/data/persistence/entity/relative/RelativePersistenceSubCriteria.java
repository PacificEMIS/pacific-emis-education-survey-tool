package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceAnswer;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;

public class RelativePersistenceSubCriteria {

    @Embedded
    public PersistenceSubCriteria subCriteria;

    @Relation(parentColumn = "uid", entityColumn = "sub_criteria_id", entity = PersistenceAnswer.class)
    public List<RelativePersistenceAnswer> answers;

}
