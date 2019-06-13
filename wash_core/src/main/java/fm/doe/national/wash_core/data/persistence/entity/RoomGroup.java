package fm.doe.national.wash_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.wash_core.data.model.Group;
import fm.doe.national.wash_core.data.model.SubGroup;

@Entity(
        foreignKeys = @ForeignKey(
                entity = RoomWashSurvey.class,
                parentColumns = "uid",
                childColumns = "survey_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("uid"),
                @Index("survey_id")
        })
public class RoomGroup implements Group {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    public String prefix;

    @ColumnInfo(name = "survey_id")
    public long surveyId;

    public RoomGroup(long surveyId, String title, String prefix) {
        this.surveyId = surveyId;
        this.title = title;
        this.prefix = prefix;
    }

    public RoomGroup(@NonNull Group other) {
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
    public List<? extends SubGroup> getSubGroups() {
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
