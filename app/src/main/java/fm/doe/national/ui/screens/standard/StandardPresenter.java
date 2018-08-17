package fm.doe.national.ui.screens.standard;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.mock.MockRepository;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    private MockStandard standard;

    public void setStandard(MockStandard standard) throws NullPointerException {
        if (standard == null) throw new NullPointerException();
        this.standard = standard;
        loadStandard();
    }

    public void onQuestionStateChanged() {
        getViewState().bindProgress(standard.getAnsweredCount(), standard.getQuestionsCount());
    }

    private void loadStandard() {
        // TODO: replace mocking

        standard = MockRepository.getStandards().get(0);

        getViewState().bindGlobalInfo(standard.getName(), standard.getIcon());
        getViewState().bindCriterias(standard.getCriterias());
        getViewState().bindProgress(standard.getAnsweredCount(), standard.getQuestionsCount());
    }
}
