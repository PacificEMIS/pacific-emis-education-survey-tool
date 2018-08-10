package fm.doe.national.ui.screens.menu.slide_menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.BaseMenuActivity;
import fm.doe.national.ui.screens.menu.base.BaseMenuPresenter;

public class SideMenuActivity extends BaseMenuActivity implements SideMenuView, DrawerLayout.DrawerListener {

    @InjectPresenter
    SideMenuPresenter sideMenuPresenter;

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.textview_school_accreditation)
    TextView schoolAccreditationTextView;
    @BindView(R.id.textview_school_data_verification)
    TextView schoolDataVerificationTextView;
    @BindView(R.id.textview_monitoring_and_evaluation)
    TextView monitoringAndEvaluationTextView;
    @BindView(R.id.textview_education_survey_tool)
    TextView educationSurveyToolTextView;

    public static Intent createIntent(Context context) {
        return new Intent(context, SideMenuActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addDrawerListener(this);
        toggle.syncState();
    }

    @Override
    protected BaseMenuPresenter getPresenter() {
        return sideMenuPresenter;
    }

    @Override
    public void hideMenu() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void selectMenuOption(MenuItems menuOption) {
        schoolAccreditationTextView.setSelected(menuOption == MenuItems.SCHOOL_ACCREDITATION);
        schoolDataVerificationTextView.setSelected(menuOption == MenuItems.SCHOOL_DATA_VERIFICATION);
        monitoringAndEvaluationTextView.setSelected(menuOption == MenuItems.MONITORING_AND_EVALUATION);
        educationSurveyToolTextView.setSelected(menuOption == MenuItems.EDUCATION_SURVEY_TOOL);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        //nothing
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        //nothing
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        //nothing
    }

    @Override
    public void showSchoolAccreditationScreen() {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        hideKeyboard();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, fragment)
                .commit();
    }

}
