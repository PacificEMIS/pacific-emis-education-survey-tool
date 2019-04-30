package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;

public class RelativePersistenceSurvey {

    @Embedded
    public PersistenceSurvey survey;

    @Relation(parentColumn = "uid", entityColumn = "survey_id", entity = PersistenceCategory.class)
    public List<RelativePersistenceCategory> categories;

}
