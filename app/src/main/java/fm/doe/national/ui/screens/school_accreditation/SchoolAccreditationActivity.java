package fm.doe.national.ui.screens.school_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.choose_category.ChooseCategoryActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

public class SchoolAccreditationActivity extends BaseDrawerActivity
        implements SchoolAccreditationView, SchoolAccreditationAdapter.Callback, View.OnClickListener {

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
    public void onAccreditationChoosen(SchoolAccreditationPassing schoolAccreditationPassing) {
        schoolAccreditationPresenter.onSchoolClicked(schoolAccreditationPassing);
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
                // TODO: nav to accreditation creation
                break;
        }
    }
}
