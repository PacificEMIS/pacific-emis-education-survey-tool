package fm.doe.national.accreditation_core.category_merge;

import org.junit.Test;

import java.util.Date;

import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObservationInfoMergeUnitTest {

    @Test
    public void test_observationInfoMerge_bothAreEmpty() {
        final MutableObservationInfo host = CategoryMergeConstants.ObservationInfo.createEmpty();
        final ObservationInfo other = CategoryMergeConstants.ObservationInfo.createEmpty();

        host.merge(other);

        assertNull(host.getTeacherName());
        assertNull(host.getGrade());
        assertNull(host.getTotalStudentsPresent());
        assertNull(host.getSubject());
        assertNull(host.getDate());
    }

    @Test
    public void test_observationInfoMerge_hostFilledOtherEmpty() {
        final MutableObservationInfo host = CategoryMergeConstants.ObservationInfo.createHostFilled();
        final ObservationInfo other = CategoryMergeConstants.ObservationInfo.createEmpty();

        host.merge(other);

        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_TEACHER_NAME, host.getTeacherName());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_GRADE, host.getGrade());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_STUDENTS_PRESENT, host.getTotalStudentsPresent());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_SUBJECT, host.getSubject());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_DATE, host.getDate());
    }

    @Test
    public void test_observationInfoMerge_hostEmptyOtherFilled() {
        final Date testDate = new Date();
        final MutableObservationInfo host = CategoryMergeConstants.ObservationInfo.createEmpty();
        final ObservationInfo other = CategoryMergeConstants.ObservationInfo.createOtherFilled();

        host.merge(other);

        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_TEACHER_NAME, host.getTeacherName());
        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_GRADE, host.getGrade());
        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_STUDENTS_PRESENT, host.getTotalStudentsPresent());
        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_SUBJECT, host.getSubject());
        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_DATE, host.getDate());
    }

    @Test
    public void test_observationInfoMerge_hostFullyFilledOtherFilled() {
        final MutableObservationInfo host = CategoryMergeConstants.ObservationInfo.createHostFilled();
        final ObservationInfo other =CategoryMergeConstants.ObservationInfo.createOtherFilled();

        host.merge(other);

        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_TEACHER_NAME, host.getTeacherName());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_GRADE, host.getGrade());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_STUDENTS_PRESENT, host.getTotalStudentsPresent());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_SUBJECT, host.getSubject());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_DATE, host.getDate());
    }

    @Test
    public void test_observationInfoMerge_hostPartiallyFilledOtherFilled() {
        final MutableObservationInfo host = CategoryMergeConstants.ObservationInfo.createEmpty();
        host.setGrade(CategoryMergeConstants.ObservationInfo.HOST_GRADE);
        host.setDate(CategoryMergeConstants.ObservationInfo.HOST_DATE);
        final ObservationInfo other = CategoryMergeConstants.ObservationInfo.createOtherFilled();

        host.merge(other);

        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_TEACHER_NAME, host.getTeacherName());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_GRADE, host.getGrade());
        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_STUDENTS_PRESENT, host.getTotalStudentsPresent());
        assertEquals(CategoryMergeConstants.ObservationInfo.OTHER_SUBJECT, host.getSubject());
        assertEquals(CategoryMergeConstants.ObservationInfo.HOST_DATE, host.getDate());
    }
}
