package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class ChooseCategoryPresenter extends BasePresenter<ChooseCategoryView> {
    private SchoolAccreditationPassing schoolAccreditationPassing;

    public ChooseCategoryPresenter(SchoolAccreditationPassing schoolAccreditationPassing) {
        this.schoolAccreditationPassing = schoolAccreditationPassing;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        List<Standard> standards = new ArrayList<>();
        for (GroupStandard groupStandard: schoolAccreditationPassing.getSchoolAccreditation().getGroupStandards()) {
            standards.addAll(groupStandard.getStandards());
        }
        getViewState().setCategories(standards);
    }

    public void onCategoryClicked(int position) {
        getViewState().showStandardScreen(schoolAccreditationPassing, position);
    }

}
