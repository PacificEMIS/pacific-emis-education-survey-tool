package org.pacific_emis.surveys.wash_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.wash_core.data.model.Question;
import org.pacific_emis.surveys.wash_core.data.model.SubGroup;

@Entity(
        foreignKeys = @ForeignKey(
                entity = RoomGroup.class,
                parentColumns = "uid",
                childColumns = "group_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("uid"),
                @Index("group_id")
        })
public class RoomSubGroup implements SubGroup {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    public String prefix;

    @ColumnInfo(name = "group_id")
    public long groupId;

    public RoomSubGroup(long groupId, String title, String prefix) {
        this.groupId = groupId;
        this.title = title;
        this.prefix = prefix;
    }

    public RoomSubGroup(@NonNull SubGroup other) {
        this.uid = other.getId();
        this.title = other.getTitle();
        this.prefix = other.getPrefix();
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    @Override
    public List<? extends Question> getQuestions() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new UnsupportedOperationException();
    }
}
