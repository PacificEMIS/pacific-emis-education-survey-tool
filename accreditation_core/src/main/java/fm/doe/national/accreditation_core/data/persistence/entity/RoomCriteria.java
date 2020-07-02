package fm.doe.national.accreditation_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.accreditation_core.data.model.SubCriteria;

@Entity(foreignKeys = @ForeignKey(entity = RoomStandard.class, parentColumns = "uid", childColumns = "standard_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("standard_id")})
public class RoomCriteria implements Criteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "standard_id")
    public long standardId;

    public RoomCriteria(String title, long standardId, String suffix) {
        this.title = title;
        this.standardId = standardId;
        this.suffix = suffix;
    }

    public RoomCriteria(@NonNull Criteria other) {
        this.uid = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();
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

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }
}

