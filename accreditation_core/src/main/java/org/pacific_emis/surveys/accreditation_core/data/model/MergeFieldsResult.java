package org.pacific_emis.surveys.accreditation_core.data.model;


import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pair;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MergeFieldsResult {

    private List<Pair<Long, MutableObservationInfo>> observationInfoList = new ArrayList<>();
    private List<MutableAnswer> answers = new ArrayList<>();
    private LongSparseArray<List<MutableObservationLogRecord>> addedLogRecords = new LongSparseArray<>();
    private LongSparseArray<List<MutableObservationLogRecord>> updatedLogRecords = new LongSparseArray<>();

    public List<MutableAnswer> getAnswers() {
        return answers;
    }

    public LongSparseArray<List<MutableObservationLogRecord>> getAddedLogRecords() {
        return addedLogRecords;
    }

    public LongSparseArray<List<MutableObservationLogRecord>> getUpdatedLogRecords() {
        return updatedLogRecords;
    }

    public List<Pair<Long, MutableObservationInfo>> getObservationInfoList() {
        return observationInfoList;
    }

    public void addObservationInfo(long categoryId, MutableObservationInfo info) {
        observationInfoList.add(Pair.create(categoryId, info));
    }

    public void addAnswers(List<MutableAnswer> answers) {
        this.answers.addAll(answers);
    }

    public void addCreatedLogRecords(long categoryId, List<MutableObservationLogRecord> records) {
        addLogRecords(addedLogRecords, categoryId, records);
    }

    public void addUpdatedLogRecords(long categoryId, List<MutableObservationLogRecord> records) {
        addLogRecords(updatedLogRecords, categoryId, records);
    }

    private void addLogRecords(LongSparseArray<List<MutableObservationLogRecord>> container,
                               long categoryId,
                               List<MutableObservationLogRecord> records) {
        if (!container.containsKey(categoryId)) {
            container.put(categoryId, new ArrayList<>());
        }
        container.get(categoryId).addAll(records);
    }

    public void plus(@NonNull MergeFieldsResult other) {
        observationInfoList.addAll(other.getObservationInfoList());
        addAnswers(other.getAnswers());
        plusRecords(addedLogRecords, other.getAddedLogRecords());
        plusRecords(updatedLogRecords, other.getUpdatedLogRecords());
    }

    private void plusRecords(LongSparseArray<List<MutableObservationLogRecord>> container,
                             LongSparseArray<List<MutableObservationLogRecord>> records) {
        CollectionUtils.forEach(records, (id, item) -> addLogRecords(container, id, item));
    }

}
