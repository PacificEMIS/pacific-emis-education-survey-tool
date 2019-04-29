package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceAnswer;
import fm.doe.national.data.persistence.entity.PersistencePhoto;

public class RelativePersistenceAnswer {

    @Embedded
    public PersistenceAnswer answer;

    @Relation(parentColumn = "uid", entityColumn = "answer_id")
    public List<PersistencePhoto> photos;

}
