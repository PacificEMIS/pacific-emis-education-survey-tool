package fm.doe.national.ui.screens.menu.base;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;

import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.shool_accreditation.SchoolAccreditationActivity;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */
public abstract class MenuDrawerActivity extends BaseDrawerActivity implements MenuDrawerView {

    protected abstract MenuDrawerPresenter getPresenter();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(layoutResID, R.layout.activity_main);
    }

    @Override
    public void showSchoolAccreditationScreen() {
        Intent intent = SchoolAccreditationActivity.createIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showSchoolDataVerificationScreen() {
        //TODO added logic
    }

    @Override
    public void shoMonitoringAndEvaluationScreen() {
        //TODO added logic
    }

    @Override
    public void showEducationSurveyToolScreen() {
        //TODO added logic
    }

}
