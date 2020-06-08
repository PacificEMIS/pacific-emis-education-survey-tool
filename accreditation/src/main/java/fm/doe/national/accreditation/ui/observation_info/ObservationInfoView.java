package fm.doe.national.accreditation.ui.observation_info;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;

import fm.doe.national.core.ui.screens.base.BaseView;

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
    void setDate(@Nullable Date date);

}
