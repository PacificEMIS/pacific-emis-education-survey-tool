package fm.doe.national.ui.screens.shool_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.choose_category.ChooseCategoryActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

public class SchoolAccreditationActivity extends BaseDrawerActivity implements SchoolAccreditationView, SchoolAccreditationAdapter.Callback {

    @InjectPresenter
    SchoolAccreditationPresenter schoolAccreditationPresenter;

    @BindView(R.id.recyclerview)
    OmegaRecyclerView recyclerView;

    private SchoolAccreditationAdapter schoolAccreditationAdapter = new SchoolAccreditationAdapter();

    public static Intent createIntent(Context context) {
        return new Intent(context, SchoolAccreditationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schoolAccreditationAdapter.setCallback(this);
        recyclerView.setAdapter(schoolAccreditationAdapter);
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
    public void onClick(SchoolAccreditationPassing schoolAccreditationPassing) {
        schoolAccreditationPresenter.onSchoolClicked(schoolAccreditationPassing);
    }

    @Override
    public void showChooseCategoryScreen(SchoolAccreditationPassing schoolAccreditationPassing) {
        startActivity(ChooseCategoryActivity.createIntent(this));
    }

    @Override
    public void setAccreditations(List<SchoolAccreditationPassing> accreditations) {
        schoolAccreditationAdapter.setItems(accreditations);
        typeTestAdapter.notifyDataSetChanged();
    }
}
