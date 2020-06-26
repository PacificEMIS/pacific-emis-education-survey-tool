package org.pacific_emis.surveys.wash_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.pacific_emis.surveys.core.data.model.Photo;

@Entity(
        foreignKeys = @ForeignKey(
                entity = RoomAnswer.class,
                parentColumns = "uid",
                childColumns = "answer_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("uid"),
                @Index("answer_id")
        })
public class RoomPhoto implements Photo {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String localUrl;

    public String remoteUrl;

    @ColumnInfo(name = "answer_id")
    public long answerId;

    public RoomPhoto(long answerId) {
        this.answerId = answerId;
    }

    public RoomPhoto(@NonNull Photo otherPhoto) {
        this.uid = otherPhoto.getId();
        this.localUrl = otherPhoto.getLocalPath();
        this.remoteUrl = otherPhoto.getRemotePath();
    }

    @Nullable
    @Override
    public String getLocalPath() {
        return localUrl;
    }

    @Nullable
    @Override
    public String getRemotePath() {
        return remoteUrl;
    }

    @Override
    public long getId() {
        return uid;
    }

}
