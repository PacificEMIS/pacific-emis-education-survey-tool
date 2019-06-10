package fm.doe.national.wash_core.data.persistence.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;
import fm.doe.national.wash_core.data.model.Variant;

@Entity(
        foreignKeys = @ForeignKey(
                entity = RoomQuestion.class,
                parentColumns = "uid",
                childColumns = "question_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("uid"),
                @Index("question_id")
        })
public class RoomAnswer implements Answer {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "question_id")
    public long questionId;

    @Nullable
    public String comment;

    @Nullable
    public String inputText;

    @Nullable
    public List<String> items;

    @Nullable
    public List<Variant> variants;

    @Nullable
    public Location location;

    @Nullable
    public BinaryAnswerState binaryAnswerState;

    @Nullable
    public TernaryAnswerState ternaryAnswerState;

    public RoomAnswer(long questionId) {
        this.questionId = questionId;
    }

    public RoomAnswer(Answer other) {
        this.uid = other.getId();
        this.comment = other.getComment();
        this.items = other.getItems();
        this.variants = other.getVariants();
        this.location = other.getLocation();
        this.binaryAnswerState = other.getBinaryAnswerState();
        this.ternaryAnswerState = other.getTernaryAnswerState();
        this.inputText = other.getInputText();
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public List<? extends Photo> getPhotos() {
        return null;
    }

    @Nullable
    @Override
    public BinaryAnswerState getBinaryAnswerState() {
        return binaryAnswerState;
    }

    @Nullable
    @Override
    public TernaryAnswerState getTernaryAnswerState() {
        return ternaryAnswerState;
    }

    @Override
    public long getId() {
        return uid;
    }

    @Nullable
    @Override
    public String getInputText() {
        return inputText;
    }
}
