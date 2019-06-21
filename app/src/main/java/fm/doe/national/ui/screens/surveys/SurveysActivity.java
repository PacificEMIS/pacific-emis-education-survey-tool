package fm.doe.national.ui.screens.surveys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.survey.ui.SurveyActivity;
import fm.doe.national.ui.screens.menu.MainMenuActivity;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyActivity;

public class SurveysActivity extends BaseActivity implements
        SurveysView,
        View.OnClickListener,
        BaseAdapter.OnItemClickListener<Survey>,
        SurveysAdapter.MenuItemClickListener {

    @InjectPresenter
    SurveysPresenter presenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

    private MenuItem becomeAvailableItem;
    private MenuItem becomeUnavailableItem;

    @Nullable
    private Runnable delayedMenuInit;

    private final SurveysAdapter surveysAdapter = new SurveysAdapter(this, this);

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SurveysActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }
    }

    private void initViews() {
        setTitle(R.string.label_school_accreditation);
        recyclerView.setAdapter(surveysAdapter);
        newAccreditationFab.setOnClickListener(this);
    }

    @Override
    public void onHomePressed() {
        startActivity(MainMenuActivity.createIntent(this, true));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_surveys;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_surveys, menu);
        becomeAvailableItem = menu.findItem(R.id.action_become_available_for_sync);
        becomeUnavailableItem = menu.findItem(R.id.action_become_unavailable_for_sync);

        if (delayedMenuInit != null) {
            delayedMenuInit.run();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export_all:
                presenter.onExportAllPressed();
                return true;
            case R.id.action_become_available_for_sync:
                presenter.onBecomeAvailableForSyncPressed();
                return true;
            case R.id.action_become_unavailable_for_sync:
                presenter.onBecomeUnavailableForSyncPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToSurvey() {
        startActivity(new Intent(this, SurveyActivity.class));
    }

    @Override
    public void setSurveys(List<Survey> accreditations) {
        surveysAdapter.setItems(accreditations);
    }

    @Override
    public void removeSurvey(Survey passing) {
        surveysAdapter.removeItem(passing);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_accreditation:
                startActivity(CreateSurveyActivity.createIntent(this));
                break;
        }
    }

    @Override
    public void onItemClick(Survey item) {
        presenter.onSurveyPressed(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem, Survey survey) {
        switch (menuItem.getItemId()) {
            case R.id.action_merge:
                presenter.onSurveyMergePressed(survey);
                return true;
            case R.id.action_export_to_excel:
                presenter.onSurveyExportToExcelPressed(survey);
                return true;
            case R.id.action_remove:
                presenter.onSurveyRemovePressed(survey);
                return true;
            default:
                return false;
        }
    }

    @Nullable
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.textview_load_survey)
    void onLoadPartiallySavedSurveyPressed() {
        presenter.onLoadPartiallySavedSurveyPressed();
    }

    @Override
    public void setConnectionState(ConnectionState connectionState) {
        delayedMenuInit = () -> {
            switch (connectionState) {
                case NONE:
                    becomeAvailableItem.setVisible(true);
                    becomeUnavailableItem.setVisible(false);
                    break;
                case CONNECTED:
                    becomeAvailableItem.setVisible(false);
                    becomeUnavailableItem.setVisible(true);
                    break;
                case LISTENING:
                case CONNECTING:
                    becomeAvailableItem.setVisible(false);
                    becomeUnavailableItem.setVisible(false);
                    break;
            }
        };

        if (becomeUnavailableItem != null) {
            delayedMenuInit.run();
            delayedMenuInit = null;
        }

    }
}
