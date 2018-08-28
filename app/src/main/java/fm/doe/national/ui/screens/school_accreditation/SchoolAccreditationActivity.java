package fm.doe.national.ui.screens.school_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.choose_category.ChooseCategoryActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyActivity;

public class SchoolAccreditationActivity extends BaseDrawerActivity implements
        SchoolAccreditationView,
        BaseClickableAdapter.OnRecyclerItemClickListener<SchoolAccreditationPassing>,
        View.OnClickListener {

    @InjectPresenter
    SchoolAccreditationPresenter schoolAccreditationPresenter;

    @BindView(R.id.recyclerview)
    OmegaRecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

    private SchoolAccreditationAdapter schoolAccreditationAdapter = new SchoolAccreditationAdapter();

    public static Intent createIntent(Context context) {
        return new Intent(context, SchoolAccreditationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schoolAccreditationAdapter.setListener(this);
        recyclerView.setAdapter(schoolAccreditationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newAccreditationFab.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_school_accreditation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected BaseDrawerPresenter getPresenter() {
        return schoolAccreditationPresenter;
    }

    @Override
    public void navigateToCategoryChooser(SchoolAccreditationPassing schoolAccreditationPassing) {
        startActivity(ChooseCategoryActivity.createIntent(this));
    }

    @Override
    public void setAccreditations(List<SchoolAccreditationPassing> accreditations) {
        schoolAccreditationAdapter.setItems(accreditations);
        typeTestAdapter.notifyDataSetChanged();
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
    public void onRecyclerItemClick(SchoolAccreditationPassing item) {
        schoolAccreditationPresenter.onAccreditationClicked(item);
    }
}
