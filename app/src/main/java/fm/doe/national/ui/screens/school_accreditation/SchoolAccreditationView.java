package fm.doe.national.ui.screens.school_accreditation;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.model.Survey;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

@StateStrategyType(AddToEndSingleStrategy.class)
interface SchoolAccreditationView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCategoryChooser(long passingId);


    void setAccreditations(List<Survey> accreditations);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSurveyDeleteConfirmation();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void removeSurveyPassing(Survey passing);
}
