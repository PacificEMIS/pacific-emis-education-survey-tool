package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.data.persistence.new_model.Criteria;
import fm.doe.national.data.persistence.new_model.SubCriteria;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceStandard.class, parentColumns = "uid", childColumns = "standard_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("standard_id")})
public class PersistenceCriteria implements Criteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "standard_id")
    public long standardId;

    @Ignore
    private List<SubCriteria> subCriterias;

    public PersistenceCriteria(String title, long standardId, String suffix) {
        this.title = title;
        this.standardId = standardId;
        this.suffix = suffix;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @NonNull
    @Override
    public List<SubCriteria> getSubCriterias() {
        return subCriterias;
    }

    @Override
    public long getId() {
        return uid;
    }

}

