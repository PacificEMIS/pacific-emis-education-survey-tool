package fm.doe.national.ui.screens.shool_accreditation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    private final List<MockStandard> standards;

    public SchoolAccreditationPresenter() {

        ArrayList<MockSchool> schools = new ArrayList<>();

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

        MockSchool school1 = new MockSchool("Ban Sam Kong School", 2018, subs1);
        schools.add(school1);
        MockSchool school2 = new MockSchool("Kajonkiet International School", 2018, subs2);
        schools.add(school2);
        MockSchool school3 = new MockSchool("The London School Of English", 2018, subs3);
        schools.add(school3);
        MockSchool school4 = new MockSchool("Dwight School London Lower School", 2018, subs1);
        schools.add(school4);
        MockSchool school5 = new MockSchool("London Lower School", 2018, subs2);
        schools.add(school5);
        MockSchool school6 = new MockSchool("DLondon Lower School", 2018, subs3);
        schools.add(school6);

        standards = new ArrayList<>();
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
        getViewState().setSchools(schools);
    }

    public void onSchoolClicked(MockSchool standards) {
        getViewState().showChooseCategoryScreen(this.standards);
    }
}
