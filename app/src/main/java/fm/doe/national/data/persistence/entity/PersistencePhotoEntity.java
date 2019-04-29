package fm.doe.national.data.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fm.doe.national.data.persistence.new_model.PhotoEntity;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceAnswer.class, parentColumns = "uid", childColumns = "answer_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("answer_id")})
public class PersistencePhotoEntity implements PhotoEntity {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String url;

    @ColumnInfo(name = "answer_id")
    public long answerId;

    public PersistencePhotoEntity(String url, long answerId) {
        this.url = url;
        this.answerId = answerId;
    }

    @Override
    public String getLocalPath() {
        return url;
    }
}
