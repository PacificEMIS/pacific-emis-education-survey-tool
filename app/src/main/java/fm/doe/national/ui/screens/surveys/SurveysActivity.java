package fm.doe.national.ui.screens.surveys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.survey.ui.SurveyActivity;
import fm.doe.national.ui.screens.menu.MainMenuActivity;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyActivity;

public class SurveysActivity extends BaseActivity implements
        SurveysView,
        SearchView.OnQueryTextListener,
        View.OnClickListener,
        BaseAdapter.OnItemClickListener<Survey>,
        SurveysAdapter.MenuItemClickListener {

    @InjectPresenter
    SurveysPresenter presenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

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
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        // SearchView is not MATCH_PARENT in Toolbar, it's bounded by MaxWidth
        // To make it MATCH_PARENT just set MaxValue to MAX_INT
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
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

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.onSearchQueryChanged(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Nullable
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}
