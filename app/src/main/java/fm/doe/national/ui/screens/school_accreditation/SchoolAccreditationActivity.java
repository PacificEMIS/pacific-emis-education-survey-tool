package fm.doe.national.ui.screens.school_accreditation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.categories.CategoriesActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyActivity;

public class SchoolAccreditationActivity extends BaseDrawerActivity implements
        SchoolAccreditationView,
        BaseAdapter.OnItemClickListener<SchoolAccreditationPassing>,
        View.OnClickListener,
        DialogInterface.OnClickListener,
        SearchView.OnQueryTextListener, BaseAdapter.OnItemLongClickListener<SchoolAccreditationPassing> {

    @InjectPresenter
    SchoolAccreditationPresenter presenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

    private final SchoolAccreditationAdapter schoolAccreditationAdapter = new SchoolAccreditationAdapter(this, this);
    private final DeleteConfirmationListener deleteConfirmationListener = new DeleteConfirmationListener();

    public static Intent createIntent(Context context) {
        return new Intent(context, SchoolAccreditationActivity.class);
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
        recyclerView.setAdapter(schoolAccreditationAdapter);
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
    public void navigateToCategoryChooser(long passingId) {
        startActivity(CategoriesActivity.createIntent(this, passingId));
    }

    @Override
    public void setAccreditations(List<SchoolAccreditationPassing> accreditations) {
        schoolAccreditationAdapter.setItems(accreditations);
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
    public void removeSurveyPassing(SchoolAccreditationPassing passing) {
        schoolAccreditationAdapter.removeItem(passing);
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
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onItemClick(SchoolAccreditationPassing item) {
        presenter.onAccreditationClicked(item);
    }

    @Override
    public void onItemLongClick(SchoolAccreditationPassing item) {
        presenter.onAccreditationLongClicked(item);
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
