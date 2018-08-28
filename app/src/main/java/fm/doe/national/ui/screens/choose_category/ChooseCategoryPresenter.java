package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ChooseCategoryPresenter extends BasePresenter<ChooseCategoryView> {
    private SchoolAccreditationPassing schoolAccreditationPassing;
    private long schoolAccreditationPassingId;

    @Inject
    DataSource dataSource;

    public ChooseCategoryPresenter(long schoolAccreditationPassingId) {
        this.schoolAccreditationPassingId = schoolAccreditationPassingId;
        MicronesiaApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        load();
    }

    public void onCategoryClicked(int position) {
        getViewState().showStandardScreen(/*schoolAccreditationPassing.getId()*/0, position);
    }

    // TODO: replace
    private void load() {
        getViewState().showProgressDialog(Text.empty());
        add(
                dataSource.requestSchoolAccreditationPassings()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(schoolAccreditationPassings -> {
                            schoolAccreditationPassing = schoolAccreditationPassings.get(0);
                            loadStandards();
                            getViewState().hideProgressDialog();
                        }, throwable -> {
                            getViewState().showWarning(
                                    Text.from(R.string.title_warning),
                                    Text.from(R.string.warn_unable_to_get_schools));
                            getViewState().hideProgressDialog();
                        })
        );
    }

    private void loadStandards() {
        List<Standard> standards = new ArrayList<>();
        for (GroupStandard groupStandard: schoolAccreditationPassing.getSchoolAccreditation().getGroupStandards()) {
            standards.addAll(groupStandard.getStandards());
        }
        getViewState().setCategories(standards);
    }

}
