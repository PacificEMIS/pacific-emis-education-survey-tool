package fm.doe.national.ui.screens.main;

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
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.main.slide_menu.SideMenuPresenter;
import fm.doe.national.ui.screens.main.slide_menu.SideMenuView;

public class MainActivity extends BaseActivity implements SideMenuView, DrawerLayout.DrawerListener {

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
        return new Intent(context, MainActivity.class);
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

    @OnClick({R.id.textview_school_accreditation, R.id.textview_school_data_verification,
            R.id.textview_monitoring_and_evaluation, R.id.textview_education_survey_tool})
    public void onMenuItemClick(View view) {
        switch (view.getId()) {
            case R.id.textview_school_accreditation:
                sideMenuPresenter.onSchoolAccreditationClicked();
                break;
            case R.id.textview_school_data_verification:
                sideMenuPresenter.onSchoolDataVerificationClicked();
                break;
            case R.id.textview_monitoring_and_evaluation:
                sideMenuPresenter.onMonitoringAndEvaluationClicked();
                break;
            case R.id.textview_education_survey_tool:
                sideMenuPresenter.onEducationSurveyToolClicked();
                break;
        }
    }

    @Override
    public void showSchoolAccreditationScreen() {
        //TODO create fragment
        // replaceFragment(SchoolAccreditationFragment.newInstance());
    }

    @Override
    public void showSchoolDataVerificationScreen() {
        //TODO create fragment
        // replaceFragment(SchoolDataVerificationFragment.newInstance());
    }

    @Override
    public void shoMonitoringAndEvaluationScreen() {
        //TODO create fragment
        //replaceFragment(MonitoringAndEvaluationFragment.newInstance());
    }

    @Override
    public void showEducationSurveyToolScreen() {
        //TODO create fragment
        //replaceFragment(EducationSurveyToolFragment.newInstance());
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
