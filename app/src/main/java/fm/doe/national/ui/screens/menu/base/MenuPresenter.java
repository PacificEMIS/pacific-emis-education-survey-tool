package fm.doe.national.ui.screens.menu.base;

import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BasePresenter;

abstract public class MenuPresenter<V extends MenuView> extends BasePresenter<V> {

    public MenuPresenter() {
        List<Text> items = new ArrayList<>();
        items.add(Text.from(R.string.title_school_accreditation));
        items.add(Text.from(R.string.title_school_data_verification));
        items.add(Text.from(R.string.title_monitoring_and_evaluation));
        getViewState().setItems(items);
    }

    public void onTypeTestClicked() {
        //TODO changed on correct
        getViewState().navigateToSchoolAccreditationScreen();
    }
}
