package org.pacific_emis.surveys.accreditation.ui.observation_info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

import java.util.Date;
import java.util.List;

public interface ObservationInfoView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrevButtonVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonEnabled(boolean isEnabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonText(Text text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setTeacherName(@Nullable String teacherName);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setGrade(@Nullable String grade);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setTotalStudentsPresent(@Nullable Integer totalStudentsPresent);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSubject(@Nullable String subject);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDate(@NonNull Date date);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDateTimePicker(@NonNull Date sourceDate, @NonNull OnDateTimePickedListener listener);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showGradeSelector(@NonNull List<String> possibleGrades, @NonNull OnGradePickedListener listener);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setTeachersToAutocompleteField(@NonNull List<Teacher> teachers);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setSubjectsToAutocompleteField(@NonNull List<Subject> subjects);

    interface OnDateTimePickedListener {
        void onDateTimePicked(@NonNull Date date);
    }

    interface OnGradePickedListener {
        void onGradePicked(@NonNull String grade);
    }

}
