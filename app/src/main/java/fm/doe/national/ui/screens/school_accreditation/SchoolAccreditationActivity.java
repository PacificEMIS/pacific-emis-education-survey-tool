package fm.doe.national.ui.screens.school_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import fm.doe.national.ui.screens.group_standards.GroupStandardsActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyActivity;

public class SchoolAccreditationActivity extends BaseDrawerActivity implements
        SchoolAccreditationView,
        BaseAdapter.OnItemClickListener<SchoolAccreditationPassing>,
        View.OnClickListener,
        SearchView.OnQueryTextListener {

    @InjectPresenter
    SchoolAccreditationPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

    private final SchoolAccreditationAdapter schoolAccreditationAdapter = new SchoolAccreditationAdapter();

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
        schoolAccreditationAdapter.setListener(this);
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
        return true;
    }

    @Override
    public void navigateToCategoryChooser(long passingId) {
        startActivity(GroupStandardsActivity.createIntent(this, passingId));
    }

    @Override
    public void setAccreditations(List<SchoolAccreditationPassing> accreditations) {
        schoolAccreditationAdapter.setItems(accreditations);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_accreditation:
                startActivity(new Intent(this, CreateSurveyActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(SchoolAccreditationPassing item) {
        presenter.onAccreditationClicked(item);
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
}
