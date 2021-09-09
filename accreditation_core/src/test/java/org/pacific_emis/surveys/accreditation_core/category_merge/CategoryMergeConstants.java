package org.pacific_emis.surveys.accreditation_core.category_merge;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;

import java.util.Date;

interface CategoryMergeConstants {

    class ObservationInfo {
        final static String HOST_TEACHER_NAME = "teacherName";
        final static Integer HOST_TEACHER_ID = 1;
        final static String HOST_GRADE = "grade";
        final static Integer HOST_STUDENTS_PRESENT = 12;
        final static String HOST_SUBJECT = "subject";
        final static Date HOST_DATE = new Date(100000);

        final static String OTHER_TEACHER_NAME = "teacherName2";
        final static Integer OTHER_TEACHER_ID = 2;
        final static String OTHER_GRADE = "grade2";
        final static Integer OTHER_STUDENTS_PRESENT = 13;
        final static String OTHER_SUBJECT = "subject2";
        final static Date OTHER_DATE = new Date(200000);
        
        @NonNull
        static MutableObservationInfo createHostFilled() {
            return new MutableObservationInfo(
                    HOST_TEACHER_NAME,
                    HOST_TEACHER_ID,
                    HOST_GRADE,
                    HOST_STUDENTS_PRESENT,
                    HOST_SUBJECT,
                    HOST_DATE
            );
        }

        @NonNull
        static MutableObservationInfo createOtherFilled() {
            return new MutableObservationInfo(
                    OTHER_TEACHER_NAME,
                    OTHER_TEACHER_ID,
                    OTHER_GRADE,
                    OTHER_STUDENTS_PRESENT,
                    OTHER_SUBJECT,
                    OTHER_DATE
            );
        }

        @NonNull
        static MutableObservationInfo createEmpty() {
            return new MutableObservationInfo();
        }
    }

    class LogRecord {
        final static long HOST_ID = 0;
        final static Date HOST_DATE = new Date(100000);
        final static String HOST_TEACHER_ACTIONS = "host teacher actions";
        final static String HOST_STUDENTS_ACTIONS = "host students actions";

        final static long OTHER_ID = 1;
        final static Date OTHER_DATE = new Date(200000);
        final static String OTHER_TEACHER_ACTIONS = "other teacher actions";
        final static String OTHER_STUDENTS_ACTIONS = "other students actions";

        @NonNull
        static MutableObservationLogRecord createHostFilled() {
            return new MutableObservationLogRecord(HOST_ID, HOST_DATE, HOST_TEACHER_ACTIONS, HOST_STUDENTS_ACTIONS);
        }

        @NonNull
        static MutableObservationLogRecord createOtherFilled() {
            return new MutableObservationLogRecord(OTHER_ID, OTHER_DATE, OTHER_TEACHER_ACTIONS, OTHER_STUDENTS_ACTIONS);
        }

        @NonNull
        static MutableObservationLogRecord createHostEmpty() {
            return new MutableObservationLogRecord(HOST_ID, HOST_DATE, null, null);
        }

        @NonNull
        static MutableObservationLogRecord createOtherEmpty() {
            return new MutableObservationLogRecord(OTHER_ID, OTHER_DATE, null, null);
        }
    }

}
