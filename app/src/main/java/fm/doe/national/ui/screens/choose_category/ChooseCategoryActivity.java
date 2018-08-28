package fm.doe.national.ui.screens.choose_category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.standard.StandardActivity;

public class ChooseCategoryActivity extends BaseActivity implements ChooseCategoryView, ChooseCategoryAdapter.Callback {

    private static final String EXTRA_PASSING_ID = "EXTRA_PASSING_ID";

    @InjectPresenter
    ChooseCategoryPresenter chooseCategoryPresenter;

    @ProvidePresenter
    ChooseCategoryPresenter providePresenter() {
        return new ChooseCategoryPresenter(getIntent().getLongExtra(EXTRA_PASSING_ID, -1));
    }

    private final ChooseCategoryAdapter chooseCategoryAdapter = new ChooseCategoryAdapter();

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @BindView(R.id.recyclerview)
    OmegaRecyclerView recyclerView;

    public static Intent createIntent(Context context, long passingId) {
        return new Intent(context, ChooseCategoryActivity.class).putExtra(EXTRA_PASSING_ID, passingId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseCategoryAdapter.setCallback(this);
        recyclerView.setAdapter(chooseCategoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressTextView.setText("1/7"); // FIXME
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_category;
    }

    @Override
    public void setCategories(List<Standard> standards) {
        chooseCategoryAdapter.setItems(standards);
    }

    @Override
    public void showStandardScreen(long passingId, int position) {
        startActivity(StandardActivity.createIntent(this, passingId));
    }

    @Override
    public void onClick(int position) {
        chooseCategoryPresenter.onCategoryClicked(position);
    }
}
