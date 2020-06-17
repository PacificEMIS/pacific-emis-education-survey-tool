package fm.doe.national.offline_sync.ui.progress;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.offline_sync.data.model.Device;

public interface ProgressView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDevice(Device device);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSurvey(Survey survey);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setMergeProgress(int progress);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDescription(Text text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTryAgain();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showContinue();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

}
