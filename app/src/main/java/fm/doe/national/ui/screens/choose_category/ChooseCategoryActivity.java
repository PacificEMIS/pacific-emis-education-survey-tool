package fm.doe.national.ui.screens.choose_category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.standard.StandardActivity;

/**
 * Created by Alexander Chibirev on 8/17/2018.
 */
public class ChooseCategoryActivity extends BaseActivity implements ChooseCategoryView, ChooseCategoryAdapter.Callback {

    @InjectPresenter
    ChooseCategoryPresenter chooseCategoryPresenter;

    private ChooseCategoryAdapter chooseCategoryAdapter;

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @BindView(R.id.recyclerview)
    OmegaRecyclerView recyclerView;

    public static Intent createIntent(Context context) {
        return new Intent(context, ChooseCategoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseCategoryAdapter = new ChooseCategoryAdapter();
        chooseCategoryAdapter.setCallback(this);
        recyclerView.setAdapter(chooseCategoryAdapter);
        progressTextView.setText("1/7");
        initToolbar();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_category;
    }

    @Override
    public void bindCategories(List<MockStandard> standards) {
        chooseCategoryAdapter.updateCategory(standards);
    }

    @Override
    public void showStandardScreen(MockStandard standard) {
        startActivity(StandardActivity.getStartingIntent(this, standard));
    }

    @Override
    public void onCategoryClicked(MockStandard standard) {
        chooseCategoryPresenter.onCategoryClicked(standard);
    }
}
