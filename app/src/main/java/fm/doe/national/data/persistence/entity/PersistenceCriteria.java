package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.SubCriteria;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceStandard.class, parentColumns = "uid", childColumns = "standard_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("standard_id")})
public class PersistenceCriteria implements Criteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "standard_id")
    public long standardId;

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

    @Nullable
    @Override
    public List<? extends SubCriteria> getSubCriterias() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

}

