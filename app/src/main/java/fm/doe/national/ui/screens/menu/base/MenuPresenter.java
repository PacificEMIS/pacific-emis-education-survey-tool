package fm.doe.national.ui.screens.menu.base;

import android.content.SharedPreferences;

import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.utils.Constants;

abstract public class MenuPresenter<V extends MenuView> extends BasePresenter<V> {

    private final SharedPreferences sharedPreferences = MicronesiaApplication.getAppComponent().getSharedPreferences();

    public MenuPresenter() {
        setupSelector();
    }

    private void setupSelector() {
        List<Text> items = new ArrayList<>();
        items.add(Text.from(R.string.title_school_accreditation));
        items.add(Text.from(R.string.title_school_data_verification));
        items.add(Text.from(R.string.title_monitoring_and_evaluation));
        getViewState().setItems(items);
    }

    private void loadLogo() {
        String logoPath = sharedPreferences.getString(Constants.PREF_KEY_LOGO_PATH, null);
        if (logoPath != null) {
            getViewState().setLogo(logoPath);
        }
    }

    public void onTypeTestClicked() {
        //TODO changed on correct
        getViewState().navigateToSchoolAccreditationScreen();
    }

    public void notifyReturnedFromBackground() {
        loadLogo();
    }
}
