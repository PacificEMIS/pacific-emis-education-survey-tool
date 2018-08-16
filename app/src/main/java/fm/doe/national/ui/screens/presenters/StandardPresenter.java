package fm.doe.national.ui.screens.presenters;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.standard.StandardView;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadCriterias();
    }

    private void loadCriterias() {
        // TODO: replace mocking
        ArrayList<MockCriteria> criterias = new ArrayList<>();

        ArrayList<MockSubCriteria> subs1 = new ArrayList<>();
        subs1.add(new MockSubCriteria("First question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs1.add(new MockSubCriteria("Second question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs1.add(new MockSubCriteria("Third question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs1.add(new MockSubCriteria("Forth question", "HINT", MockSubCriteria.State.NOT_ANSWERED));

        criterias.add(new MockCriteria("1.1. Criteria the First", subs1));

        ArrayList<MockSubCriteria> subs2 = new ArrayList<>();
        subs2.add(new MockSubCriteria("1question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("2question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("3question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("4question", "HINT", MockSubCriteria.State.NOT_ANSWERED));

        criterias.add(new MockCriteria("1.2. Criteria the Second", subs2));

        ArrayList<MockSubCriteria> subs3 = new ArrayList<>();
        subs3.add(new MockSubCriteria("long question long question long question long question long question long question long question long question long question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("2question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long questionyet another long questionyet another long questionyet another long question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("4question", "HINT", MockSubCriteria.State.NOT_ANSWERED));

        criterias.add(new MockCriteria("1.3. Some long criteria title that should use multiline for debugSome long criteria title that should use multiline for debugSome long criteria title that should use multiline for debug", subs3));

        getViewState().bindCriterias(criterias);
    }
}
