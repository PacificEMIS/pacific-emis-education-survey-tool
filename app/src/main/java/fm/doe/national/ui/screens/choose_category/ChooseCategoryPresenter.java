package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.view_data.SubCriteriaViewData;

/**
 * Created by Alexander Chibirev on 8/17/2018.
 */

@InjectViewState
public class ChooseCategoryPresenter extends BasePresenter<ChooseCategoryView> {

    public ChooseCategoryPresenter() {
        //TODO changed logic after correct work data base
        ArrayList<MockStandard> standards = new ArrayList<>();
    }

    public void onCategoryClicked(SchoolAccreditationResult standard) {
        getViewState().showStandardScreen(standard);
    }

}
