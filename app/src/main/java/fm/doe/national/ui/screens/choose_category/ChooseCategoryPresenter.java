package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/17/2018.
 */

@InjectViewState
public class ChooseCategoryPresenter extends BasePresenter<ChooseCategoryView> {

    public ChooseCategoryPresenter() {
        //TODO changed logic after correct work data base
        ArrayList<MockStandard> standards = new ArrayList<>();
    }

    public void onCategoryClicked(SchoolAccreditationPassing standard) {
        getViewState().showStandardScreen(standard);
    }

}
