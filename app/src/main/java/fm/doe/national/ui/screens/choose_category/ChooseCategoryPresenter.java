package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.R;
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/17/2018.
 */

@InjectViewState
public class ChooseCategoryPresenter extends BasePresenter<ChooseCategoryView> {

    public ChooseCategoryPresenter() {
        ArrayList<MockStandard> standards = new ArrayList<>();

        ArrayList<MockSubCriteria> subs1 = new ArrayList<>();
        subs1.add(new MockSubCriteria("First question", "HINT", MockSubCriteria.State.POSITIVE));
        subs1.add(new MockSubCriteria("Second question", "HINT", MockSubCriteria.State.POSITIVE));
        subs1.add(new MockSubCriteria("Third question", "HINT", MockSubCriteria.State.POSITIVE));
        subs1.add(new MockSubCriteria("Forth question", "HINT", MockSubCriteria.State.POSITIVE));

        ArrayList<MockSubCriteria> subs2 = new ArrayList<>();
        subs2.add(new MockSubCriteria("1question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("2question", "HINT", MockSubCriteria.State.NEGATIVE));
        subs2.add(new MockSubCriteria("3question", "HINT", MockSubCriteria.State.NEGATIVE));
        subs2.add(new MockSubCriteria("4question", "HINT", MockSubCriteria.State.NEGATIVE));

        ArrayList<MockSubCriteria> subs3 = new ArrayList<>();
        subs3.add(new MockSubCriteria("long question long question long question long question long question long question long question long question long question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("2question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long questionyet another long questionyet another long questionyet another long question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("4question", "HINT", MockSubCriteria.State.POSITIVE));


        ArrayList<MockCriteria> criterias = new ArrayList<>();
        criterias.add(new MockCriteria("1.1. Criteria the First", subs1));
        criterias.add(new MockCriteria("1.1. Criteria the Sec", subs2));
        criterias.add(new MockCriteria("1.1. Criteria the Tr", subs3));
        MockStandard standard1 = new MockStandard("Leadership", criterias);
        standard1.setIcon(R.drawable.ic_action_mic);
        MockStandard standard2 = new MockStandard("Teacher Performance", criterias);
        MockStandard standard3 = new MockStandard("Data Management", criterias);
        MockStandard standard4 = new MockStandard("National Curriculum", criterias);
        standards.add(standard1);
        standards.add(standard2);
        standards.add(standard3);
        standards.add(standard4);
        getViewState().bindCategories(standards);
    }

    public void onCategoryClicked(MockStandard standard) {
        getViewState().showStandardScreen(standard);
    }

}
