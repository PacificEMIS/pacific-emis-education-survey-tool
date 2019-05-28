package fm.doe.national.ui.screens.all_surveys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.categories.CategoriesActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyActivity;

public class AllSurveysActivity extends BaseDrawerActivity implements
        AllSurveysView,
        SearchView.OnQueryTextListener,
        View.OnClickListener,
        BaseAdapter.OnItemClickListener<Survey>,
        AllSurveysAdapter.MenuItemClickListener {

    @InjectPresenter
    AllSurveysPresenter presenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

    private final AllSurveysAdapter allSurveysAdapter = new AllSurveysAdapter(this, this);
    private final DeleteConfirmationListener deleteConfirmationListener = new DeleteConfirmationListener();

    public static Intent createIntent(Context context) {
        return new Intent(context, AllSurveysActivity.class);
    }

    @Override
    protected BaseDrawerPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.label_school_accreditation);
        recyclerView.setAdapter(allSurveysAdapter);
        newAccreditationFab.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_school_accreditation;
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
    public void navigateToCategoryChooser() {
        // TODO: this is future navigation
//        startActivity(new Intent(this, SurveyActivity.class));
        startActivity(CategoriesActivity.createIntent(this));
    }

    @Override
    public void setSurveys(List<Survey> accreditations) {
        allSurveysAdapter.setItems(accreditations);
    }

    @Override
    public void showSurveyDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_delete_confirmation)
                .setMessage(R.string.survey_delete_confirmation)
                .setPositiveButton(R.string.yes, deleteConfirmationListener)
                .setNegativeButton(R.string.no, deleteConfirmationListener)
                .create()
                .show();
    }

    @Override
    public void removeSurvey(Survey passing) {
        allSurveysAdapter.removeItem(passing);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_accreditation:
                startActivity(CreateSurveyActivity.createIntent(this));
                break;
            default:
                super.onClick(view);
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

    private class DeleteConfirmationListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    presenter.onSurveyDeletionConfirmed();
                    break;
                default:
                    dialog.dismiss();
            }

        }
    }
}
