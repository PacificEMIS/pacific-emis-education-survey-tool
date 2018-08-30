package fm.doe.national.ui.screens.survey_creation;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CreateSurveyView extends BaseView {

    void setSchools(List<School> schools);

    void setYear(int year);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCategoryChooser(long passingId);

}
