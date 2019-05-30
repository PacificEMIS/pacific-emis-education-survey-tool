package fm.doe.national.accreditation.ui.questions;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface QuestionsView extends BaseView {

    void setQuestions(List<Question> questions);

    void showCommentEditor(String comment);

    void showPhotos();
}
