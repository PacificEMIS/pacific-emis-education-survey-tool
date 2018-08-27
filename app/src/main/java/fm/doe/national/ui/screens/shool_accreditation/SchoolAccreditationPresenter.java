package fm.doe.national.ui.screens.shool_accreditation;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    private final List<MockStandard> standards = new ArrayList<>();

    public SchoolAccreditationPresenter() {
    }

    public void onSchoolClicked(SchoolAccreditationPassing schoolAccreditationPassing) {
        getViewState().showChooseCategoryScreen(schoolAccreditationPassing);
    }

}
