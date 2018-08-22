package fm.doe.national.ui.screens.shool_accreditation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    private final List<MockStandard> standards = new ArrayList<>();

    public SchoolAccreditationPresenter() {
        //TODO changed logic after correct work data base
        getViewState().setSchools(new ArrayList<>());
    }

    public void onSchoolClicked(MockSchool standards) {
        getViewState().showChooseCategoryScreen(this.standards);
    }

}
