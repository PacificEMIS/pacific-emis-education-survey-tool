package fm.doe.national.ui.screens.survey_creation;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.group_standards.GroupStandardsActivity;

public class CreateSurveyActivity extends BaseActivity implements
        CreateSurveyView,
        BaseClickableAdapter.OnRecyclerItemClickListener<School>,
        SearchView.OnQueryTextListener {

    @BindView(R.id.textview_year)
    TextView yearTextView;

    @BindView(R.id.imageview_edit)
    ImageView editImageView;

    @BindView(R.id.recyclerview_schools)
    OmegaRecyclerView schoolsRecycler;

    @InjectPresenter
    CreateSurveyPresenter presenter;

    private final SchoolsAdapter adapter = new SchoolsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        schoolsRecycler.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_create_survey;
    }

    @Override
    public void setSchools(List<School> schools) {
        adapter.setItems(schools);
    }

    @Override
    public void setYear(int year) {
        yearTextView.setText(String.valueOf(year));
    }

    @Override
    public void navigateToCategoryChooser(long passingId) {
        startActivity(GroupStandardsActivity.createIntent(this, passingId));
    }

    @Override
    public void onRecyclerItemClick(School item) {
        presenter.onSchoolPicked(item);
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
    public boolean onQueryTextChange(String newText) {
        presenter.onSearchQueryChanged(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
