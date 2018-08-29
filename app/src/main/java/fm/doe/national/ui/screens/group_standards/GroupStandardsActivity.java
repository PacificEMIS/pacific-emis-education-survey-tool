package fm.doe.national.ui.screens.group_standards;

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
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.standard.StandardActivity;

public class GroupStandardsActivity extends BaseActivity implements GroupStandardsView {

    private static final String EXTRA_PASSING_ID = "EXTRA_PASSING_ID";

    private final GroupStandardsAdapter groupStandardsAdapter = new GroupStandardsAdapter();
    private final StandardAdapter standardAdapter = new StandardAdapter();

    @InjectPresenter
    GroupStandardsPresenter groupStandardsPresenter;

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @BindView(R.id.recyclerview)
    OmegaRecyclerView recyclerView;

    private boolean isShowingGroups;

    public static Intent createIntent(Context context, long passingId) {
        return new Intent(context, GroupStandardsActivity.class).putExtra(EXTRA_PASSING_ID, passingId);
    }

    @ProvidePresenter
    GroupStandardsPresenter providePresenter() {
        return new GroupStandardsPresenter(getIntent().getLongExtra(EXTRA_PASSING_ID, -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupStandardsAdapter.setListener(this::onGroupClicked);
        standardAdapter.setListener(this::onStandardClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_category;
    }

    @Override
    public void showGroupStandards(List<GroupStandard> groups) {
        groupStandardsAdapter.setItems(groups);
        recyclerView.setAdapter(groupStandardsAdapter);
        isShowingGroups = true;
    }

    @Override
    public void showStandards(List<Standard> standards) {
        standardAdapter.setItems(standards);
        recyclerView.setAdapter(standardAdapter);
        isShowingGroups = false;
    }

    @Override
    public void navigateToStandardScreen(long passingId, int position) {
        startActivity(StandardActivity.createIntent(this, passingId, new long[] {}));
    }

    @Override
    public void setGlobalProgress(int completed, int total) {
        progressTextView.setText(getString(R.string.criteria_progress, completed, total));
    }

    public void onGroupClicked(GroupStandard item) {
        groupStandardsPresenter.onGroupClicked(item);
    }

    public void onStandardClicked(Standard item) {
        groupStandardsPresenter.onStandardClicked(item);
    }

    @Override
    public void onBackPressed() {
        performUpAction(super::onBackPressed);
    }

    @Override
    public void onHomePressed() {
        performUpAction(super::onHomePressed);
    }

    private void performUpAction(Runnable upRunnable) {
        if (isShowingGroups) {
            upRunnable.run();
        } else {
            recyclerView.setAdapter(groupStandardsAdapter);
            isShowingGroups = true;
        }
    }
}
