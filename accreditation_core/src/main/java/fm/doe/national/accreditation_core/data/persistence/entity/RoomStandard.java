package fm.doe.national.accreditation_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationInfo;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.accreditation_core.data.model.Standard;

@Entity(foreignKeys = @ForeignKey(entity = RoomCategory.class, parentColumns = "uid", childColumns = "category_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("category_id")})
public class RoomStandard implements Standard {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "category_id")
    public long categoryId;

    @Nullable
    @ColumnInfo(name = "observation_info_teacher_name")
    public String observationInfoTeacherName;

    @Nullable
    @ColumnInfo(name = "observation_info_grade")
    public String observationInfoGrade;

    @Nullable
    @ColumnInfo(name = "observation_info_students_present")
    public Integer observationInfoTotalStudentsPresent;

    @Nullable
    @ColumnInfo(name = "observation_info_subject")
    public String observationInfoSubject;

    @Nullable
    @ColumnInfo(name = "observation_info_date")
    public Date observationInfoDate;

    public RoomStandard(
            String title,
            long categoryId,
            String suffix,
            @Nullable String observationInfoTeacherName,
            @Nullable String observationInfoGrade,
            @Nullable Integer observationInfoTotalStudentsPresent,
            @Nullable String observationInfoSubject,
            @Nullable Date observationInfoDate
    ) {
        this.title = title;
        this.categoryId = categoryId;
        this.suffix = suffix;
        this.observationInfoTeacherName = observationInfoTeacherName;
        this.observationInfoGrade = observationInfoGrade;
        this.observationInfoTotalStudentsPresent = observationInfoTotalStudentsPresent;
        this.observationInfoSubject = observationInfoSubject;
        this.observationInfoDate = observationInfoDate;
    }

    public RoomStandard(@NonNull Standard other) {
        this.uid = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();

        final ObservationInfo otherObservationInfo = other.getObservationInfo();
        if (otherObservationInfo != null) {
            this.observationInfoTeacherName = otherObservationInfo.getTeacherName();
            this.observationInfoGrade = otherObservationInfo.getGrade();
            this.observationInfoTotalStudentsPresent = otherObservationInfo.getTotalStudentsPresent();
            this.observationInfoSubject = otherObservationInfo.getSubject();
            this.observationInfoDate = otherObservationInfo.getDate();
        }
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
    public List<? extends Criteria> getCriterias() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

    @Nullable
    @Override
    public ObservationInfo getObservationInfo() {
        if (haveObservationInfo()) {
            return new MutableObservationInfo(
                    observationInfoTeacherName,
                    observationInfoGrade,
                    observationInfoTotalStudentsPresent,
                    observationInfoSubject,
                    observationInfoDate
            );
        } else {
            return null;
        }
    }

    private boolean haveObservationInfo() {
        return observationInfoTeacherName != null
                || observationInfoGrade != null
                || observationInfoTotalStudentsPresent != null
                || observationInfoSubject != null
                || observationInfoDate != null;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }
}
