package org.pacific_emis.surveys.accreditation_core.category_merge;

import org.junit.Test;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ObservationLogRecordMergeUnitTest {

    @Test
    public void test_observationInfoMerge_bothHaveNulls() {
        final MutableObservationLogRecord host = CategoryMergeConstants.LogRecord.createHostEmpty();
        final MutableObservationLogRecord other = CategoryMergeConstants.LogRecord.createOtherEmpty();

        host.merge(other);

        assertNotNull(host.getDate());
        assertNull(host.getTeacherActions());
        assertNull(host.getStudentsActions());
    }

    @Test
    public void test_observationInfoMerge_hostFilledOtherEmpty() {
        final MutableObservationLogRecord host = CategoryMergeConstants.LogRecord.createHostFilled();
        final MutableObservationLogRecord other = CategoryMergeConstants.LogRecord.createOtherEmpty();

        host.merge(other);

        assertEquals(CategoryMergeConstants.LogRecord.HOST_ID, host.getId());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_DATE, host.getDate());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_TEACHER_ACTIONS, host.getTeacherActions());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_STUDENTS_ACTIONS, host.getStudentsActions());
    }

    @Test
    public void test_observationInfoMerge_hostEmptyOtherFilled() {
        final MutableObservationLogRecord host = CategoryMergeConstants.LogRecord.createHostEmpty();
        final MutableObservationLogRecord other = CategoryMergeConstants.LogRecord.createOtherFilled();

        host.merge(other);

        assertEquals(CategoryMergeConstants.LogRecord.HOST_ID, host.getId());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_DATE, host.getDate());
        assertEquals(CategoryMergeConstants.LogRecord.OTHER_TEACHER_ACTIONS, host.getTeacherActions());
        assertEquals(CategoryMergeConstants.LogRecord.OTHER_STUDENTS_ACTIONS, host.getStudentsActions());
    }

    @Test
    public void test_observationInfoMerge_hostFullyFilledOtherFilled() {
        final MutableObservationLogRecord host = CategoryMergeConstants.LogRecord.createHostFilled();
        final MutableObservationLogRecord other = CategoryMergeConstants.LogRecord.createOtherFilled();

        host.merge(other);

        assertEquals(CategoryMergeConstants.LogRecord.HOST_ID, host.getId());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_DATE, host.getDate());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_TEACHER_ACTIONS, host.getTeacherActions());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_STUDENTS_ACTIONS, host.getStudentsActions());
    }

    @Test
    public void test_observationInfoMerge_hostPartiallyFilledOtherFilled() {
        final MutableObservationLogRecord host = CategoryMergeConstants.LogRecord.createHostEmpty();
        host.setStudentsActions(CategoryMergeConstants.LogRecord.HOST_STUDENTS_ACTIONS);
        final MutableObservationLogRecord other = CategoryMergeConstants.LogRecord.createOtherFilled();

        host.merge(other);

        assertEquals(CategoryMergeConstants.LogRecord.HOST_ID, host.getId());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_DATE, host.getDate());
        assertEquals(CategoryMergeConstants.LogRecord.OTHER_TEACHER_ACTIONS, host.getTeacherActions());
        assertEquals(CategoryMergeConstants.LogRecord.HOST_STUDENTS_ACTIONS, host.getStudentsActions());
    }
}
