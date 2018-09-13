package fm.doe.national.ui.screens.survey_creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.categories.CategoriesActivity;

public class CreateSurveyActivity extends BaseActivity implements
        CreateSurveyView,
        BaseAdapter.OnItemClickListener<School>,
        SearchView.OnQueryTextListener {

    private final SchoolsListAdapter adapter = new SchoolsListAdapter();

    @BindView(R.id.textview_year)
    TextView yearTextView;

    @BindView(R.id.button_edit)
    Button editButton;

    @BindView(R.id.recyclerview_schools)
    RecyclerView schoolsRecycler;

    @InjectPresenter
    CreateSurveyPresenter presenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, CreateSurveyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.label_create_survey);

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
        startActivity(CategoriesActivity.createIntent(this, passingId));
    }

    @Override
    public void onItemClick(School item) {
        presenter.onSchoolPicked(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
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
