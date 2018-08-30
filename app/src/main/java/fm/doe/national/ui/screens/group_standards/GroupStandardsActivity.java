package fm.doe.national.ui.screens.group_standards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.standard.StandardActivity;

public class GroupStandardsActivity extends BaseActivity implements GroupStandardsView {

    private static final String EXTRA_PASSING_ID = "EXTRA_PASSING_ID";

    private final GroupStandardsListAdapter groupStandardsAdapter = new GroupStandardsListAdapter();
    private final StandardListAdapter standardAdapter = new StandardListAdapter();

    @InjectPresenter
    GroupStandardsPresenter groupStandardsPresenter;

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @BindView(R.id.recyclerview_groups)
    RecyclerView groupsRecyclerView;

    @BindView(R.id.recyclerview_standards)
    RecyclerView standardsRecyclerView;

    public static Intent createIntent(Context context, long passingId) {
        return new Intent(context, GroupStandardsActivity.class)
                .putExtra(EXTRA_PASSING_ID, passingId);
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
        groupsRecyclerView.setAdapter(groupStandardsAdapter);
        standardsRecyclerView.setAdapter(standardAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_category;
    }

    @Override
    public void showGroupStandards(List<GroupStandard> groups) {
        groupStandardsAdapter.setItems(groups);
        groupsRecyclerView.setVisibility(View.VISIBLE);
        standardsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showStandards(List<Standard> standards) {
        standardAdapter.setItems(standards);
        groupsRecyclerView.setVisibility(View.GONE);
        standardsRecyclerView.setVisibility(View.VISIBLE);
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
        if (groupsRecyclerView.getVisibility() == View.VISIBLE) {
            upRunnable.run();
        } else {
            groupsRecyclerView.setVisibility(View.VISIBLE);
            standardsRecyclerView.setVisibility(View.GONE);
        }
    }
}
