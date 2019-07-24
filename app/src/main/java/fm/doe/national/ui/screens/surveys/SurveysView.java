package fm.doe.national.ui.screens.surveys;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothView;

@StateStrategyType(AddToEndSingleStrategy.class)
interface SurveysView extends BaseBluetoothView {

    void setTitle(Text title);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurvey();

    void setSurveys(List<Survey> accreditations);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void removeSurvey(Survey passing);

    void setExportEnabled(boolean isEnabled);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openInExternalApp(String url);
}
