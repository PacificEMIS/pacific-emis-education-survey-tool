package fm.doe.national.offline_sync.ui.surveys;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.Survey;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SyncSurveysView {

    void setSurveys(List<Survey> surveys);

}
