package fm.doe.national.ui.dialogs.merge_progress;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MergeProgressView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

    void setProgress(int progress);

    void setDescription(Text text);

}
