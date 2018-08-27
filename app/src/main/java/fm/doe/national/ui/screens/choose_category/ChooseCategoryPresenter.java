package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class ChooseCategoryPresenter extends BasePresenter<ChooseCategoryView> {
    private SchoolAccreditationPassing schoolAccreditationPassing;

    public ChooseCategoryPresenter(SchoolAccreditationPassing schoolAccreditationPassing) {
        this.schoolAccreditationPassing = schoolAccreditationPassing;
    }

    public void onCategoryClicked(int position) {
        getViewState().showStandardScreen(schoolAccreditationPassing, position);
    }

}
