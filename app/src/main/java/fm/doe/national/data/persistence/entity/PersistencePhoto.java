package fm.doe.national.data.persistence.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import fm.doe.national.data.model.Photo;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceAnswer.class, parentColumns = "uid", childColumns = "answer_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("answer_id")})
public class PersistencePhoto implements Photo {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String localUrl;

    public String remoteUrl;

    @ColumnInfo(name = "answer_id")
    public long answerId;

    public PersistencePhoto(long answerId) {
        this.answerId = answerId;
    }

    @Nullable
    @Override
    public String getLocalPath() {
        return localUrl;
    }

    @Override
    public void setLocalPath(@Nullable String path) {
        localUrl = path;
    }

    @Nullable
    @Override
    public String getRemotePath() {
        return remoteUrl;
    }

    @Override
    public void setRemotePath(@Nullable String remotePath) {
        remoteUrl = remotePath;
    }

    @Override
    public long getId() {
        return uid;
    }

}
