package fm.doe.national.offline_sync.ui.progress;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.offline_sync.data.model.Device;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ProgressView extends BaseView {

    void setDevice(Device device);

    void setSurvey(Survey survey);

    void setMergeProgress(int progress);

    void setDescription(Text text);

    void showTryAgainButton();

    void showContinueButton();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

}
