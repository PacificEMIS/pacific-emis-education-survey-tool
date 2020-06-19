package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import fm.doe.national.core.data.model.IdentifiedObject;

public interface ObservationLogRecord extends IdentifiedObject {

    Date getDate();

    void setDate(@NonNull Date date);

    @Nullable
    String getTeacherActions();

    @Nullable
    String getStudentsActions();

}
