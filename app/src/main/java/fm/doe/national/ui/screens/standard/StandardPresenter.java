package fm.doe.national.ui.screens.standard;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.mock.MockRepository;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BasePresenter;

// TODO: replace mocking
@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    private MockStandard standard;
    private MockStandard previousStandard;
    private MockStandard nextStandard;

    public void setStandard(MockStandard standard) throws NullPointerException {
        if (standard == null) throw new NullPointerException();
        this.standard = standard;
        loadStandard();
    }

    public void onQuestionStateChanged() {
        getViewState().bindProgress(standard.getAnsweredCount(), standard.getQuestionsCount());
    }

    public void onNextPressed() {
        getViewState().navigateToOtherStandard(nextStandard);
    }

    public void onPreviousPressed() {
        getViewState().navigateToOtherStandard(previousStandard);
    }

    private void loadStandard() {
        if (standard.getName() == null) {
            standard = MockRepository.getStandards().get(0);
        }
        previousStandard = MockRepository.getPreviousStandard(standard);
        nextStandard = MockRepository.getNextStandard(standard);

        getViewState().bindGlobalInfo(standard.getName(), standard.getIcon());
        getViewState().bindCriterias(standard.getCriterias());
        getViewState().bindProgress(standard.getAnsweredCount(), standard.getQuestionsCount());
        getViewState().bindPrevStandard(previousStandard.getName(), previousStandard.getIconHighlighted());
        getViewState().bindNextStandard(nextStandard.getName(), nextStandard.getIconHighlighted());
    }
}
