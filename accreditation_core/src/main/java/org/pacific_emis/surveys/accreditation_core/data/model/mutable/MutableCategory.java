package org.pacific_emis.surveys.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.EvaluationForm;
import org.pacific_emis.surveys.accreditation_core.data.model.MergeFieldsResult;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.Standard;
import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.mutable.BaseMutableEntity;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;
    private MutableProgress progress = MutableProgress.createEmptyProgress();
    private EvaluationForm evaluationForm;
    @Nullable
    private MutableObservationInfo observationInfo;
    private ArrayList<MutableObservationLogRecord> logRecords = new ArrayList<>();

    @NonNull
    public static MutableCategory from(@NonNull Category other) {
        if (other instanceof MutableCategory) {
            return (MutableCategory) other;
        }
        return new MutableCategory(other);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public MutableCategory() {
        // empty constructor for unit tests
    }

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.evaluationForm = other.getEvaluationForm();
        if (other.getStandards() != null) {
            this.standards = other.getStandards().stream().map(MutableStandard::new).collect(Collectors.toList());
        }

        final ObservationInfo otherObservationInfo = other.getObservationInfo();
        if (otherObservationInfo != null) {
            this.observationInfo = MutableObservationInfo.from(otherObservationInfo);
        }

        final List<? extends ObservationLogRecord> logRecords = other.getLogRecords();
        if (logRecords != null) {
            logRecords.forEach(it -> this.logRecords.add(MutableObservationLogRecord.from(it)));
        }
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public List<MutableStandard> getStandards() {
        return standards;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStandards(List<MutableStandard> standards) {
        this.standards = standards;
    }

    @NonNull
    public MutableProgress getProgress() {
        return progress;
    }

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }

    @Override
    public EvaluationForm getEvaluationForm() {
        return evaluationForm;
    }

    public void setEvaluationForm(EvaluationForm evaluationForm) {
        this.evaluationForm = evaluationForm;
    }

    public void setObservationInfo(@Nullable MutableObservationInfo observationInfo) {
        this.observationInfo = observationInfo;
    }

    @Nullable
    @Override
    public MutableObservationInfo getObservationInfo() {
        return observationInfo;
    }

    @NonNull
    @Override
    public ArrayList<MutableObservationLogRecord> getLogRecords() {
        return logRecords;
    }

    public void setLogRecords(@NonNull ArrayList<MutableObservationLogRecord> logRecords) {
        this.logRecords = logRecords;
    }

    public MergeFieldsResult merge(Category other, ConflictResolveStrategy strategy) {
        final MergeFieldsResult mergeResult = new MergeFieldsResult();

        List<? extends Standard> externalStandards = other.getStandards();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalStandards)) {
            for (Standard standard : externalStandards) {
                for (MutableStandard mutableStandard : getStandards()) {
                    if (mutableStandard.getSuffix().equals(standard.getSuffix())) {
                        changedAnswers.addAll(mutableStandard.merge(standard, strategy));
                        break;
                    }
                }
            }
        }

        mergeResult.addAnswers(changedAnswers);

        if (evaluationForm == EvaluationForm.CLASSROOM_OBSERVATION) {
            final ObservationInfo otherObservationInfo = other.getObservationInfo();
            if (otherObservationInfo != null) {
                if (this.observationInfo == null) {
                    this.observationInfo = MutableObservationInfo.from(otherObservationInfo);
                    mergeResult.addObservationInfo(id, this.observationInfo);
                } else {
                    final boolean haveChanges = this.observationInfo.merge(otherObservationInfo);
                    if (haveChanges) {
                        mergeResult.addObservationInfo(id, this.observationInfo);
                    }
                }
            }

            final List<? extends ObservationLogRecord> otherLogRecordList = other.getLogRecords();
            if (otherLogRecordList != null) {
                mergeResult.plus(mergeLogRecords(id, otherLogRecordList));
            }
        }

        return mergeResult;
    }

    private MergeFieldsResult mergeLogRecords(long categoryId, @NonNull List<? extends ObservationLogRecord> others) {
        final MergeFieldsResult mergeResult = new MergeFieldsResult();

        if (CollectionUtils.isEmpty(logRecords)) {
            logRecords = others.stream()
                    .map(MutableObservationLogRecord::from)
                    .collect(Collectors.toCollection(ArrayList::new));
            mergeResult.addCreatedLogRecords(categoryId, logRecords);
            return mergeResult;
        }

        for (ObservationLogRecord otherRecord : others) {
            final Optional<MutableObservationLogRecord> sameDateRecordOptional = logRecords.stream()
                    .filter(it -> it.getDate().equals(otherRecord.getDate()))
                    .findFirst();
            if (sameDateRecordOptional.isPresent()) {
                final MutableObservationLogRecord sameDateRecord = sameDateRecordOptional.get();
                sameDateRecord.merge(otherRecord);
                mergeResult.addUpdatedLogRecords(categoryId, Collections.singletonList(sameDateRecord));
            } else {
                final MutableObservationLogRecord mutableRecord = MutableObservationLogRecord.from(otherRecord);
                mergeResult.addCreatedLogRecords(categoryId, Collections.singletonList(mutableRecord));
                logRecords.add(mutableRecord);
            }
        }

        return mergeResult;
    }
}
