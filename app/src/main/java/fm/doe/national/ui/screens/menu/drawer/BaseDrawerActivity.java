package fm.doe.national.ui.screens.menu.drawer;

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

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.MenuActivity;

public abstract class BaseDrawerActivity extends MenuActivity implements BaseDrawerView, DrawerLayout.DrawerListener {

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.textview_education_survey_tool)
    TextView educationSurveyToolTextView;

    @BindView(R.id.textview_settings)
    TextView settingsTextView;

    private ActionBarDrawerToggle drawerToggle;

    protected abstract BaseDrawerPresenter getPresenter();

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        drawerLayout = new DrawerLayout(this);
        super.setContentView(drawerLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = getLayoutInflater();

        inflater.inflate(getContentView(), drawerLayout);

        View menuView = inflater.inflate(R.layout.activity_main, drawerLayout, false);
        menuView.setClickable(true);


        int width = getResources().getDimensionPixelSize(R.dimen.drawer_menu_width);
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.START;
        menuView.setLayoutParams(params);

        drawerLayout.addView(menuView, params);

        initToolbar();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void hideMenu() {
        drawerLayout.closeDrawers();
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

    @OnClick(R.id.textview_education_survey_tool)
    public void onEducationSurveyToolClicked() {
        getPresenter().onEducationSurveyToolClicked();
    }

    @OnClick(R.id.textview_settings)
    public void onSettingClicked() {
        getPresenter().onSettingClicked();
    }

    @Override
    public void showEducationSurveyToolScreen() {
        educationSurveyToolTextView.setSelected(true);
        //TODO added logic afters creating Education Survey Tool screen
    }

    @Override
    public void showSettingsScreen() {
        settingsTextView.setSelected(true);
        //TODO added logic afters creating settings screen
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        hideKeyboard();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

}
