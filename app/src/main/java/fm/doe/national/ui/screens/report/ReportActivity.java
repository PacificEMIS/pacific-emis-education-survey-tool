package fm.doe.national.ui.screens.report;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.DateUtils;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.report.levels.LevelsFragment;
import fm.doe.national.ui.screens.report.recommendations.RecommendationsFragment;
import fm.doe.national.ui.screens.report.summary.SummaryFragment;

public class ReportActivity extends BaseActivity implements ReportView {

    private final LegendAdapter legendAdapter = new LegendAdapter();

    @InjectPresenter
    ReportPresenter presenter;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.textview_school_code)
    TextView schoolIdTextView;

    @BindView(R.id.textview_visit_date_code)
    TextView visitDateTextView;

    @BindView(R.id.textview_school_name)
    TextView schoolNameTextView;

    @BindView(R.id.textview_principal_name)
    TextView principalNameTextView;

    @BindView(R.id.recyclerview_levels)
    RecyclerView legendRecyclerView;

    private ReportTabsPagerAdapter tabsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabsPagerAdapter = new ReportTabsPagerAdapter(
                this,
                new SummaryFragment(),
                new RecommendationsFragment(),
                new LevelsFragment()
        );
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        legendRecyclerView.setAdapter(legendAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }

    @Override
    public void setSchoolId(String id) {
        schoolIdTextView.setText(id);
    }

    @Override
    public void setSchoolName(String name) {
        schoolNameTextView.setText(name);
    }

    @Override
    public void setPrincipalName(String name) {
        principalNameTextView.setText(name);
    }

    @Override
    public void setDateOfAccreditation(Date date) {
        visitDateTextView.setText(DateUtils.format(date));
    }

    @Override
    public void setLegend(List<ReportLevel> levels) {
        legendAdapter.setItems(levels);
    }
}
