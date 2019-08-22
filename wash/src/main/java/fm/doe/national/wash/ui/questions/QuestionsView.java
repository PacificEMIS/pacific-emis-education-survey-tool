package fm.doe.national.wash.ui.questions;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface QuestionsView extends BaseView {

    void setQuestions(List<MutableQuestion> questions);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCommentEditor(Question question);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPhotos();

    void setPrevButtonVisible(boolean isVisible);

    void setNextButtonVisible(boolean isVisible);

    void setNextButtonEnabled(boolean isEnabled);

    void setNextButtonText(Text text);

    void setHintTextVisible(boolean isVisible);

    void refreshQuestionAtPosition(int selectedQuestionPosition);

}
