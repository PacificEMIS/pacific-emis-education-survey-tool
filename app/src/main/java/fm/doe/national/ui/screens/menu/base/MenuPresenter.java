package fm.doe.national.ui.screens.menu.base;

import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.ui.screens.menu.MainMenuItem;

abstract public class MenuPresenter<V extends MenuView> extends BasePresenter<V> {

    private static final int ITEM_SCHOOL_ACCREDITATION = R.string.title_school_accreditation;
    private static final int ITEM_DATA_VERIFICATION = R.string.title_school_data_verification;
    private static final int ITEM_MONITORING_AND_EVALUATION = R.string.title_monitoring_and_evaluation;
    private static final int ITEM_HYGIENE = R.string.title_water_sanitation_and_hygiene;

    private final GlobalPreferences globalPreferences = MicronesiaApplication.getAppComponent().getGlobalPreferences();

    public MenuPresenter() {
        setupSelector();
    }

    private void setupSelector() {
        List<MainMenuItem> items = new ArrayList<>();
        items.add(new MainMenuItem(ITEM_SCHOOL_ACCREDITATION));
        items.add(new MainMenuItem(ITEM_DATA_VERIFICATION));
        items.add(new MainMenuItem(ITEM_MONITORING_AND_EVALUATION));
        items.add(new MainMenuItem(ITEM_HYGIENE));
        getViewState().setItems(items);
    }

    private void loadLogo() {
        String logoPath = globalPreferences.getLogoPath();
        if (logoPath != null) {
            getViewState().setLogo(logoPath);
        }
    }

    public void onTypeTestClicked(MainMenuItem item) {
        switch (item.resId) {
            case ITEM_SCHOOL_ACCREDITATION:
                getViewState().navigateToSchoolAccreditationScreen();
                break;
            case ITEM_DATA_VERIFICATION:
            case ITEM_MONITORING_AND_EVALUATION:
            case ITEM_HYGIENE:
                getViewState().showMessage(Text.empty(), Text.from(R.string.coming_soon));
                break;
        }
    }

    public void notifyReturnedFromBackground() {
        loadLogo();
    }
}
