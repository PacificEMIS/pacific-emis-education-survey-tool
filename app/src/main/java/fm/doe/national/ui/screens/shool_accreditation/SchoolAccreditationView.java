package fm.doe.national.ui.screens.shool_accreditation;

import java.util.List;

import fm.doe.national.mock.MockSchool;
import fm.doe.national.models.survey.School;
import fm.doe.national.ui.screens.menu.base.MenuDrawerView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

interface SchoolAccreditationView extends MenuDrawerView {
    void bindSchools(List<MockSchool> schools);
}
