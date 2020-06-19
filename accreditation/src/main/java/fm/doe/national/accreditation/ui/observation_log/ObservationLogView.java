package fm.doe.national.accreditation.ui.observation_log;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.core.ui.screens.base.BaseView;

public interface ObservationLogView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrevButtonVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonEnabled(boolean isEnabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonText(Text text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateLog(List<MutableObservationLogRecord> items);

}
