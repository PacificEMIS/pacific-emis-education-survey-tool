package org.pacific_emis.surveys.wash.ui.questions;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.ui.screens.base.BaseView;
import org.pacific_emis.surveys.wash_core.data.model.Question;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableQuestion;

public interface QuestionsView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setQuestions(List<MutableQuestion> questions);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCommentEditor(Question question);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPhotos();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrevButtonVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonEnabled(boolean isEnabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonText(Text text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setHintTextVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void refreshQuestionAtPosition(int selectedQuestionPosition);

}
