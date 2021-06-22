package org.pacific_emis.surveys.remote_data;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.remote_data.models.School;

import java.io.IOException;
import java.util.List;

public interface RemoteData {
    List<School> getListOfSchoolNamesAndCodesFrom(ApiContext context) throws IOException;

    @NonNull
    List<Teacher> getListOfTeachersFrom(ApiContext context) throws IOException;

    @NonNull
    List<Subject> getListOfSubjectsFrom(ApiContext context) throws IOException;
}
