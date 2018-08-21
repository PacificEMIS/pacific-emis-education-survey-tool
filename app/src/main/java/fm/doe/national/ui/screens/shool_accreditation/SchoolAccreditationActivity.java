package fm.doe.national.ui.screens.shool_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.choose_category.ChooseCategoryActivity;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

public class SchoolAccreditationActivity extends BaseDrawerActivity implements SchoolAccreditationView, SchoolAccreditationAdapter.Callback {

    @InjectPresenter
    SchoolAccreditationPresenter schoolAccreditationPresenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

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
    public void showChooseCategoryScreen(List<MockStandard> standards) {
        startActivity(ChooseCategoryActivity.createIntent(this));
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
    public void setSchools(ArrayList<MockSchool> schools) {
        schoolAccreditationAdapter.updateSchools(schools);
        typeTestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSchoolClicked(MockSchool school) {
        schoolAccreditationPresenter.onSchoolClicked(school);
    }

}
