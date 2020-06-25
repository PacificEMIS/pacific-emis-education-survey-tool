package fm.doe.national.accreditation_core.category_merge;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.ObservationLogRecord;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationInfo;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.mutable.MutableProgress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CategoryMergeUnitTest {

    private final static List<MutableObservationLogRecord> HOST_LOGS;
    private final static List<MutableObservationLogRecord> OTHER_LOGS_WITHOUT_CONFLICT;
    private final static List<MutableObservationLogRecord> OTHER_LOGS_WITH_CONFLICT;

    private final static long RESOLVED_LOG_RECORD_ID = 1;
    private final static Date RESOLVED_LOG_RECORD_DATE = new Date(10);
    private final static String RESOLVED_LOG_RECORD_TEACHER_ACTIONS = "Ends the conflicting lesson";
    private final static String RESOLVED_LOG_RECORD_STUDENTS_ACTIONS = "students do something";

    private final static MutableObservationLogRecord RESOLVED_LOG_RECORD = new MutableObservationLogRecord(
            RESOLVED_LOG_RECORD_ID,
            RESOLVED_LOG_RECORD_DATE,
            RESOLVED_LOG_RECORD_TEACHER_ACTIONS,
            RESOLVED_LOG_RECORD_STUDENTS_ACTIONS
    );

    static {
        final MutableObservationLogRecord hostLog1 = new MutableObservationLogRecord(
                0,
                new Date(1),
                "Starts the lesson",
                "Greets the teacher"
        );
        final MutableObservationLogRecord hostLog2 = new MutableObservationLogRecord(
                RESOLVED_LOG_RECORD_ID,
                RESOLVED_LOG_RECORD_DATE,
                null,
                RESOLVED_LOG_RECORD_STUDENTS_ACTIONS
        );
        final MutableObservationLogRecord hostLog3 = new MutableObservationLogRecord(
                2,
                new Date(100),
                "Ends the lesson",
                null
        );

        HOST_LOGS = Arrays.asList(hostLog1, hostLog2, hostLog3);

        final MutableObservationLogRecord otherLog1 = new MutableObservationLogRecord(
                0,
                new Date(2),
                "Starts the other lesson",
                "Greets the other teacher"
        );
        final MutableObservationLogRecord otherLog2 = new MutableObservationLogRecord(
                1,
                new Date(11),
                "Draws scheme on desk",
                "Asking questions"
        );
        final MutableObservationLogRecord otherLog3 = new MutableObservationLogRecord(
                2,
                new Date(101),
                "Other class ends",
                "Runs home"
        );

        OTHER_LOGS_WITHOUT_CONFLICT = Arrays.asList(otherLog1, otherLog2, otherLog3);

        final MutableObservationLogRecord conflictOtherLog = new MutableObservationLogRecord(
                3,
                RESOLVED_LOG_RECORD_DATE,
                RESOLVED_LOG_RECORD_TEACHER_ACTIONS,
                "Hope there will by no errors"
        );

        OTHER_LOGS_WITH_CONFLICT = Arrays.asList(otherLog1, otherLog2, otherLog3, conflictOtherLog);
    }

    @Test
    public void test_categoryMerge_info_bothAreNull() {
        testInfoBothAreNull(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE);
        testInfoBothAreNull(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS);
        testInfoBothAreNull(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE);
        testInfoBothAreNull(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS);
    }

    private void testInfoBothAreNull(EvaluationForm evaluationForm, ConflictResolveStrategy strategy) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);

        host.merge(other, strategy);

        assertNull(host.getObservationInfo());
    }

    @Test
    public void test_categoryMerge_info_hostFilledOtherNull() {
        final OnMergeDoneCallback callback = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNotNull(info);
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_TEACHER_NAME, info.getTeacherName());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_GRADE, info.getGrade());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_STUDENTS_PRESENT, info.getTotalStudentsPresent());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_SUBJECT, info.getSubject());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_DATE, info.getDate());
        };
        testInfoHostFilledOtherNull(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, callback);
        testInfoHostFilledOtherNull(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, callback);
        testInfoHostFilledOtherNull(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, callback);
        testInfoHostFilledOtherNull(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, callback);
    }

    private void testInfoHostFilledOtherNull(EvaluationForm evaluationForm,
                                             ConflictResolveStrategy strategy,
                                             OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setObservationInfo(CategoryMergeConstants.ObservationInfo.createHostFilled());
        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setObservationInfo(null);

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_info_hostNullOtherFilled() {
        final OnMergeDoneCallback callbackForClassroomObservation = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNotNull(info);
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_TEACHER_NAME, info.getTeacherName());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_GRADE, info.getGrade());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_STUDENTS_PRESENT, info.getTotalStudentsPresent());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_SUBJECT, info.getSubject());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_DATE, info.getDate());
        };
        final OnMergeDoneCallback callbackForSchoolEvaluation = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNull(info);
        };
        testInfoHostNullOtherFilled(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, callbackForClassroomObservation);
        testInfoHostNullOtherFilled(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, callbackForClassroomObservation);
        testInfoHostNullOtherFilled(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, callbackForSchoolEvaluation);
        testInfoHostNullOtherFilled(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, callbackForSchoolEvaluation);
    }

    private void testInfoHostNullOtherFilled(EvaluationForm evaluationForm,
                                             ConflictResolveStrategy strategy,
                                             OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setObservationInfo(null);
        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setObservationInfo(CategoryMergeConstants.ObservationInfo.createOtherFilled());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_info_hostEmptyOtherFilled() {
        final OnMergeDoneCallback callbackForClassroomObservation = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNotNull(info);
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_TEACHER_NAME, info.getTeacherName());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_GRADE, info.getGrade());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_STUDENTS_PRESENT, info.getTotalStudentsPresent());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_SUBJECT, info.getSubject());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_DATE, info.getDate());
        };
        final OnMergeDoneCallback callbackForSchoolEvaluation = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNotNull(info);
            assertNull(info.getTeacherName());
            assertNull(info.getGrade());
            assertNull(info.getTotalStudentsPresent());
            assertNull(info.getSubject());
            assertNull(info.getDate());
        };
        testInfoHostEmptyOtherFilled(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, callbackForClassroomObservation);
        testInfoHostEmptyOtherFilled(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, callbackForClassroomObservation);
        testInfoHostEmptyOtherFilled(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, callbackForSchoolEvaluation);
        testInfoHostEmptyOtherFilled(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, callbackForSchoolEvaluation);
    }

    private void testInfoHostEmptyOtherFilled(EvaluationForm evaluationForm,
                                              ConflictResolveStrategy strategy,
                                              OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setObservationInfo(CategoryMergeConstants.ObservationInfo.createEmpty());
        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setObservationInfo(CategoryMergeConstants.ObservationInfo.createOtherFilled());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_info_hostPartiallyFilledOtherFilled() {
        final OnMergeDoneCallback callbackForClassroomObservation = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNotNull(info);
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_TEACHER_NAME, info.getTeacherName());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_GRADE, info.getGrade());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_STUDENTS_PRESENT, info.getTotalStudentsPresent());
            assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_SUBJECT, info.getSubject());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_DATE, info.getDate());
        };
        final OnMergeDoneCallback callbackForSchoolEvaluation = result -> {
            final ObservationInfo info = result.getObservationInfo();
            assertNotNull(info);
            assertNull(info.getTeacherName());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_GRADE, info.getGrade());
            assertNull(info.getTotalStudentsPresent());
            assertNull(info.getSubject());
            assertEquals(CategoryMergeConstants.ObservationInfo.HOST_DATE, info.getDate());
        };
        testInfoHostPartiallyFilledOtherFilled(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, callbackForClassroomObservation);
        testInfoHostPartiallyFilledOtherFilled(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, callbackForClassroomObservation);
        testInfoHostPartiallyFilledOtherFilled(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, callbackForSchoolEvaluation);
        testInfoHostPartiallyFilledOtherFilled(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, callbackForSchoolEvaluation);
    }

    private void testInfoHostPartiallyFilledOtherFilled(EvaluationForm evaluationForm,
                                                        ConflictResolveStrategy strategy,
                                                        OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);

        final MutableObservationInfo hostInfo = CategoryMergeConstants.ObservationInfo.createEmpty();
        hostInfo.setGrade(CategoryMergeConstants.ObservationInfo.HOST_GRADE);
        hostInfo.setDate(CategoryMergeConstants.ObservationInfo.HOST_DATE);

        host.setObservationInfo(hostInfo);

        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setObservationInfo(CategoryMergeConstants.ObservationInfo.createOtherFilled());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_log_hostEmptyOtherNull() {
        final OnMergeDoneCallback callback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertTrue(records.isEmpty());
        };
        testLogHostEmptyOtherNull(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, callback);
        testLogHostEmptyOtherNull(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, callback);
        testLogHostEmptyOtherNull(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, callback);
        testLogHostEmptyOtherNull(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, callback);
    }

    private void testLogHostEmptyOtherNull(EvaluationForm evaluationForm,
                                           ConflictResolveStrategy strategy,
                                           OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setLogRecords(new ArrayList<>());

        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setLogRecords(null);

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_log_hostNotEmptyOtherEmpty() {
        final OnMergeDoneCallback callback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertEquals(3, records.size());
            for (int i = 0; i < records.size(); i++) {
                assertEquals(HOST_LOGS.get(i).getDate(), records.get(i).getDate());
                assertEquals(HOST_LOGS.get(i).getTeacherActions(), records.get(i).getTeacherActions());
                assertEquals(HOST_LOGS.get(i).getStudentsActions(), records.get(i).getStudentsActions());
            }
        };
        testLogHostNotEmptyOtherEmpty(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, callback);
        testLogHostNotEmptyOtherEmpty(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, callback);
        testLogHostNotEmptyOtherEmpty(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, callback);
        testLogHostNotEmptyOtherEmpty(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, callback);
    }

    private void testLogHostNotEmptyOtherEmpty(EvaluationForm evaluationForm,
                                               ConflictResolveStrategy strategy,
                                               OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setLogRecords(createHostRecords());

        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setLogRecords(new ArrayList<>());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_log_hostEmptyOtherNotEmpty() {
        final OnMergeDoneCallback classroomObservationCallback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertEquals(3, records.size());
            for (int i = 0; i < records.size(); i++) {
                assertEquals(OTHER_LOGS_WITHOUT_CONFLICT.get(i).getDate(), records.get(i).getDate());
                assertEquals(OTHER_LOGS_WITHOUT_CONFLICT.get(i).getTeacherActions(), records.get(i).getTeacherActions());
                assertEquals(OTHER_LOGS_WITHOUT_CONFLICT.get(i).getStudentsActions(), records.get(i).getStudentsActions());
            }
        };
        final OnMergeDoneCallback schoolEvaluationCallback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertTrue(records.isEmpty());
        };
        testLogHostEmptyOtherNotEmpty(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, classroomObservationCallback);
        testLogHostEmptyOtherNotEmpty(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, classroomObservationCallback);
        testLogHostEmptyOtherNotEmpty(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, schoolEvaluationCallback);
        testLogHostEmptyOtherNotEmpty(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, schoolEvaluationCallback);
    }

    private void testLogHostEmptyOtherNotEmpty(EvaluationForm evaluationForm,
                                               ConflictResolveStrategy strategy,
                                               OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setLogRecords(new ArrayList<>());

        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setLogRecords(createOtherRecords());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_log_bothNotEmptyNoConflict() {
        final OnMergeDoneCallback classroomObservationCallback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertEquals(6, records.size());
            for (int i = 0; i < records.size(); i++) {
                final List<MutableObservationLogRecord> recordsOriginals;
                int index = i;
                if (i < 3) {
                    recordsOriginals = HOST_LOGS;
                } else {
                    recordsOriginals = OTHER_LOGS_WITHOUT_CONFLICT;
                    index -= 3;
                }
                assertEquals(recordsOriginals.get(index).getDate(), records.get(i).getDate());
                assertEquals(recordsOriginals.get(index).getTeacherActions(), records.get(i).getTeacherActions());
                assertEquals(recordsOriginals.get(index).getStudentsActions(), records.get(i).getStudentsActions());
            }
        };
        final OnMergeDoneCallback schoolEvaluationCallback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertEquals(3, records.size());
            for (int i = 0; i < records.size(); i++) {
                assertEquals(HOST_LOGS.get(i).getDate(), records.get(i).getDate());
                assertEquals(HOST_LOGS.get(i).getTeacherActions(), records.get(i).getTeacherActions());
                assertEquals(HOST_LOGS.get(i).getStudentsActions(), records.get(i).getStudentsActions());
            }
        };
        testLogBothNotEmptyNoConflict(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, classroomObservationCallback);
        testLogBothNotEmptyNoConflict(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, classroomObservationCallback);
        testLogBothNotEmptyNoConflict(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, schoolEvaluationCallback);
        testLogBothNotEmptyNoConflict(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, schoolEvaluationCallback);
    }

    private void testLogBothNotEmptyNoConflict(EvaluationForm evaluationForm,
                                               ConflictResolveStrategy strategy,
                                               OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setLogRecords(createHostRecords());

        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setLogRecords(createOtherRecords());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @Test
    public void test_categoryMerge_log_bothNotEmptyConflicted() {
        final OnMergeDoneCallback classroomObservationCallback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertEquals(6, records.size());
            for (int i = 0; i < records.size(); i++) {
                if (i == 1) {
                    assertEquals(RESOLVED_LOG_RECORD.getDate(), records.get(i).getDate());
                    assertEquals(RESOLVED_LOG_RECORD.getTeacherActions(), records.get(i).getTeacherActions());
                    assertEquals(RESOLVED_LOG_RECORD.getStudentsActions(), records.get(i).getStudentsActions());
                    continue;
                }
                final List<MutableObservationLogRecord> recordsOriginals;
                int index = i;
                if (i < 3) {
                    recordsOriginals = HOST_LOGS;
                } else {
                    recordsOriginals = OTHER_LOGS_WITHOUT_CONFLICT;
                    index -= 3;
                }
                assertEquals(recordsOriginals.get(index).getDate(), records.get(i).getDate());
                assertEquals(recordsOriginals.get(index).getTeacherActions(), records.get(i).getTeacherActions());
                assertEquals(recordsOriginals.get(index).getStudentsActions(), records.get(i).getStudentsActions());
            }
        };
        final OnMergeDoneCallback schoolEvaluationCallback = result -> {
            final List<? extends ObservationLogRecord> records = result.getLogRecords();
            assertNotNull(records);
            assertEquals(3, records.size());
            for (int i = 0; i < records.size(); i++) {
                assertEquals(HOST_LOGS.get(i).getDate(), records.get(i).getDate());
                assertEquals(HOST_LOGS.get(i).getTeacherActions(), records.get(i).getTeacherActions());
                assertEquals(HOST_LOGS.get(i).getStudentsActions(), records.get(i).getStudentsActions());
            }
        };
        testLogBothNotEmptyConflicted(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.MINE, classroomObservationCallback);
        testLogBothNotEmptyConflicted(EvaluationForm.CLASSROOM_OBSERVATION, ConflictResolveStrategy.THEIRS, classroomObservationCallback);
        testLogBothNotEmptyConflicted(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.MINE, schoolEvaluationCallback);
        testLogBothNotEmptyConflicted(EvaluationForm.SCHOOL_EVALUATION, ConflictResolveStrategy.THEIRS, schoolEvaluationCallback);
    }

    private void testLogBothNotEmptyConflicted(EvaluationForm evaluationForm,
                                               ConflictResolveStrategy strategy,
                                               OnMergeDoneCallback callback) {
        final MutableCategory host = createClearHost();
        host.setEvaluationForm(evaluationForm);
        host.setLogRecords(createHostRecords());

        final MutableCategory other = createClearOther();
        other.setEvaluationForm(evaluationForm);
        other.setLogRecords(createOtherRecordsWithConflict());

        host.merge(other, strategy);

        callback.onMergeDone(host);
    }

    @NonNull
    private MutableCategory createClearHost() {
        final MutableCategory host = new MutableCategory();
        host.setId(0);
        host.setTitle("host");
        host.setProgress(MutableProgress.createEmptyProgress());
        host.setStandards(Collections.emptyList());
        host.setLogRecords(new ArrayList<>());
        host.setObservationInfo(null);
        host.setEvaluationForm(EvaluationForm.CLASSROOM_OBSERVATION);
        return host;
    }

    @NonNull
    private MutableCategory createClearOther() {
        final MutableCategory other = new MutableCategory();
        other.setId(1);
        other.setTitle("other");
        other.setProgress(MutableProgress.createEmptyProgress());
        other.setStandards(Collections.emptyList());
        other.setLogRecords(new ArrayList<>());
        other.setObservationInfo(null);
        other.setEvaluationForm(EvaluationForm.CLASSROOM_OBSERVATION);
        return other;
    }

    @NonNull
    private ArrayList<MutableObservationLogRecord> createHostRecords() {
        return copyItems(HOST_LOGS);
    }

    @NonNull
    private ArrayList<MutableObservationLogRecord> createOtherRecords() {
        return copyItems(OTHER_LOGS_WITHOUT_CONFLICT);
    }

    @NonNull
    private ArrayList<MutableObservationLogRecord> createOtherRecordsWithConflict() {
        return copyItems(OTHER_LOGS_WITH_CONFLICT);
    }

    private ArrayList<MutableObservationLogRecord> copyItems(List<MutableObservationLogRecord> original) {
        return original.stream().map(MutableObservationLogRecord::copyOf).collect(Collectors.toCollection(ArrayList::new));
    }

    private interface OnMergeDoneCallback {
        void onMergeDone(MutableCategory result);
    }
}
