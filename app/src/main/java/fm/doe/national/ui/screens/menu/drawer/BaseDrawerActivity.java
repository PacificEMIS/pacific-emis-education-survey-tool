package fm.doe.national.ui.screens.menu.drawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.BaseMenuActivity;
import fm.doe.national.ui.screens.menu.base.BaseMenuPresenter;
import fm.doe.national.ui.screens.shool_accreditation.SchoolAccreditationActivity;

public abstract class BaseDrawerActivity extends BaseMenuActivity implements BaseDrawerView, DrawerLayout.DrawerListener {

    @InjectPresenter
    BaseDrawerPresenter baseDrawerPresenter;

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
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
    }

    protected void setContentView(@LayoutRes int contentLayoutResID, @LayoutRes int menuLayoutResID) {
        drawerLayout = new DrawerLayout(this);
        super.setContentView(drawerLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = getLayoutInflater();

        inflater.inflate(contentLayoutResID, drawerLayout);

        View menuView = inflater.inflate(menuLayoutResID, drawerLayout, false);
        menuView.setClickable(true);


        int width = getResources().getDimensionPixelSize(R.dimen.drawer_menu_width);
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.START;
        menuView.setLayoutParams(params);

        drawerLayout.addView(menuView, params);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        initToolbar();
    }

    @Override
    protected BaseMenuPresenter getPresenter() {
        return baseDrawerPresenter;
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
        Intent intent = SchoolAccreditationActivity.createIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        hideKeyboard();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

}
