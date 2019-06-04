package fm.doe.national.accreditation.ui.questions;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface QuestionsView extends BaseView {

    void setQuestions(List<Question> questions);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCommentEditor(SubCriteria subCriteria);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPhotos(long categoryId, long standardId, long criteriaId, long subCriteriaId);

    void setPrevButtonVisible(boolean isVisible);

    void setNextButtonEnabled(boolean isEnabled);

    void setNextButtonText(Text text);

    void setHintTextVisible(boolean isVisible);

}
