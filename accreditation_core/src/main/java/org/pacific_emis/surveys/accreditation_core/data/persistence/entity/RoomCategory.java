package org.pacific_emis.surveys.accreditation_core.data.persistence.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.EvaluationForm;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.Standard;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.core.data.model.Progress;

import java.util.Date;
import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = RoomAccreditationSurvey.class, parentColumns = "uid", childColumns = "survey_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("survey_id")})
public class RoomCategory implements Category {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    @ColumnInfo(name = "survey_id")
    public long surveyId;

    @ColumnInfo(name = "evaluation_form")
    public EvaluationForm evaluationForm;

    @Nullable
    @ColumnInfo(name = "observation_info_teacher_name")
    public String observationInfoTeacherName;

    @Nullable
    @ColumnInfo(name = "observation_info_teacher_id")
    public Integer observationInfoTeacherId;

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

    public RoomCategory(String title,
                        long surveyId,
                        EvaluationForm evaluationForm,
                        @Nullable String observationInfoTeacherName,
                        @Nullable Integer observationInfoTeacherId,
                        @Nullable String observationInfoGrade,
                        @Nullable Integer observationInfoTotalStudentsPresent,
                        @Nullable String observationInfoSubject,
                        @Nullable Date observationInfoDate) {
        this.title = title;
        this.surveyId = surveyId;
        this.evaluationForm = evaluationForm;
        this.observationInfoTeacherName = observationInfoTeacherName;
        this.observationInfoTeacherId = observationInfoTeacherId;
        this.observationInfoGrade = observationInfoGrade;
        this.observationInfoTotalStudentsPresent = observationInfoTotalStudentsPresent;
        this.observationInfoSubject = observationInfoSubject;
        this.observationInfoDate = observationInfoDate;
    }

    public RoomCategory(@NonNull Category other) {
        this.uid = other.getId();
        this.title = other.getTitle();
        this.evaluationForm = other.getEvaluationForm();

        final ObservationInfo otherObservationInfo = other.getObservationInfo();
        if (otherObservationInfo != null) {
            this.observationInfoTeacherName = otherObservationInfo.getTeacherName();
            this.observationInfoTeacherId = otherObservationInfo.getTeacherId();
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

    @Nullable
    @Override
    public List<? extends Standard> getStandards() {
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

    @Override
    public EvaluationForm getEvaluationForm() {
        return evaluationForm;
    }

    @Nullable
    @Override
    public ObservationInfo getObservationInfo() {
        if (haveObservationInfo()) {
            return new MutableObservationInfo(
                    observationInfoTeacherName,
                    observationInfoTeacherId,
                    observationInfoGrade,
                    observationInfoTotalStudentsPresent,
                    observationInfoSubject,
                    observationInfoDate
            );
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public List<? extends ObservationLogRecord> getLogRecords() {
        return null;
    }

    private boolean haveObservationInfo() {
        return observationInfoTeacherName != null
                || observationInfoTeacherId != null
                || observationInfoGrade != null
                || observationInfoTotalStudentsPresent != null
                || observationInfoSubject != null
                || observationInfoDate != null;
    }
}
