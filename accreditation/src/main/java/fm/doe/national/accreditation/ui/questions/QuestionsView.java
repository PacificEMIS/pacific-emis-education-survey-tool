package fm.doe.national.accreditation.ui.questions;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.core.ui.screens.base.BaseView;

public interface QuestionsView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setQuestions(List<Question> questions);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCommentEditor(SubCriteria subCriteria);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPhotos();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrevButtonVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonEnabled(boolean isEnabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonText(Text text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setHintTextVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void refreshQuestionAtPosition(int selectedQuestionPosition);

}
