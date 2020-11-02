package org.pacific_emis.surveys.wash_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import org.pacific_emis.surveys.wash_core.data.model.Answer;
import org.pacific_emis.surveys.wash_core.data.model.Question;
import org.pacific_emis.surveys.wash_core.data.model.QuestionType;
import org.pacific_emis.surveys.wash_core.data.serialization.model.Relation;
import org.pacific_emis.surveys.wash_core.data.model.Variant;

@Entity(
        foreignKeys = @ForeignKey(
                entity = RoomSubGroup.class,
                parentColumns = "uid",
                childColumns = "sub_group_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("uid"),
                @Index("sub_group_id")
        })
public class RoomQuestion implements Question {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    public String prefix;

    @ColumnInfo(name = "question_type")
    public QuestionType questionType;

    public List<Variant> variants;

    public List<String> items;

    public Relation relation;

    @ColumnInfo(name = "sub_group_id")
    public long subGroupId;

    public RoomQuestion(long subGroupId, String title, String prefix, QuestionType questionType) {
        this.title = title;
        this.prefix = prefix;
        this.questionType = questionType;
        this.subGroupId = subGroupId;
    }

    public RoomQuestion(Question other) {
        this.uid = other.getId();
        this.title = other.getTitle();
        this.prefix = other.getPrefix();
        this.questionType = other.getType();
        this.variants = other.getVariants();
        this.items = other.getItems();
        this.relation = other.getRelation();
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

    @NonNull
    @Override
    public QuestionType getType() {
        return questionType;
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Relation getRelation() {
        return relation;
    }

    @Nullable
    @Override
    public Answer getAnswer() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

}
