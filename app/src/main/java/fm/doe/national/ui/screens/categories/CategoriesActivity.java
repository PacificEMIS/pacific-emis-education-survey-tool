package fm.doe.national.ui.screens.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.standards.StandardsActivity;

public class CategoriesActivity extends BaseActivity implements CategoriesView, BaseAdapter.OnItemClickListener<GroupStandard> {

    private static final String EXTRA_PASSING_ID = "EXTRA_PASSING_ID";

    private final CategoriesListAdapter categoriesListAdapter = new CategoriesListAdapter();

    @InjectPresenter
    CategoriesPresenter categoriesPresenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView categoriesRecyclerView;


    public static Intent createIntent(Context context, long passingId) {
        return new Intent(context, CategoriesActivity.class)
                .putExtra(EXTRA_PASSING_ID, passingId);
    }

    @ProvidePresenter
    CategoriesPresenter providePresenter() {
        return new CategoriesPresenter(getIntent().getLongExtra(EXTRA_PASSING_ID, -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesListAdapter.setListener(this);
        categoriesRecyclerView.setAdapter(categoriesListAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_categories;
    }

    @Override
    public void showGroupStandards(List<GroupStandard> groups) {
        categoriesListAdapter.setItems(groups);
    }

    @Override
    public void setSurveyYear(int year) {
        setToolbarYear(year);
    }

    @Override
    public void setSchoolName(String schoolName) {
        setTitle(schoolName);
    }

    @Override
    public void onItemClick(GroupStandard item) {
        categoriesPresenter.onCategoryClicked(item);
    }

    @Override
    public void navigateToStandardsScreen(long passingId, long categoryId) {
        startActivity(StandardsActivity.createIntent(this, passingId, categoryId));
    }
}
