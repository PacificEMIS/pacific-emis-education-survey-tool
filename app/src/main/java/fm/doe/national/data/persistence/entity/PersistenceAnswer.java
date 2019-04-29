package fm.doe.national.data.persistence.entity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fm.doe.national.data.persistence.new_model.Answer;
import fm.doe.national.data.persistence.new_model.PhotoEntity;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceSubCriteria.class, parentColumns = "uid", childColumns = "sub_criteria_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("sub_criteria_id")})
public class PersistenceAnswer implements Answer {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "sub_criteria_id")
    public long subCriteriaId;

    public State state;

    @Nullable
    public String comment;

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Override
    public List<PhotoEntity> getPhotos() {
        return null;
    }

    @Override
    public void setPhotos(List<PhotoEntity> photos) {

    }
}
