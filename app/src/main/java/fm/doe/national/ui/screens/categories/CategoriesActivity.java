package fm.doe.national.ui.screens.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.Category;
import fm.doe.national.ui.custom_views.summary.SummaryView;
import fm.doe.national.ui.custom_views.summary.SummaryViewData;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.standards.StandardsActivity;

public class CategoriesActivity extends BaseActivity implements CategoriesView, BaseAdapter.OnItemClickListener<Category> {

    private final CategoriesListAdapter categoriesListAdapter = new CategoriesListAdapter(this);

    @InjectPresenter
    CategoriesPresenter categoriesPresenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView categoriesRecyclerView;

    @BindView(R.id.summaryview)
    SummaryView summaryView;

    @BindView(R.id.progressbar_summary_loading)
    View summaryLoadingView;

    public static Intent createIntent(Context context) {
        return new Intent(context, CategoriesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesRecyclerView.setAdapter(categoriesListAdapter);
        summaryView.setVisibility(View.GONE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_categories;
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoriesListAdapter.setItems(categories);
    }

    @Override
    public void setSurveyDate(Date date) {
        setToolbarDate(date);
    }

    @Override
    public void setSchoolName(String schoolName) {
        setTitle(schoolName);
    }

    @Override
    public void onItemClick(Category item) {
        categoriesPresenter.onCategoryClicked(item);
    }

    @Override
    public void navigateToStandardsScreen(long categoryId) {
        startActivity(StandardsActivity.createIntent(this, categoryId));
    }

    @Override
    public void setSummaryData(List<SummaryViewData> data) {
        summaryView.setData(data);
    }

    @Override
    public void showSummaryLoading() {
        summaryLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSummaryLoading() {
        summaryLoadingView.setVisibility(View.GONE);
        summaryView.setVisibility(View.VISIBLE);
    }
}
