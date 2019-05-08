package fm.doe.national.ui.screens.school_accreditation;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

@StateStrategyType(AddToEndSingleStrategy.class)
interface SchoolAccreditationView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCategoryChooser();

    void setAccreditations(List<MutableSurvey> accreditations);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSurveyDeleteConfirmation();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void removeSurveyPassing(MutableSurvey passing);
}
