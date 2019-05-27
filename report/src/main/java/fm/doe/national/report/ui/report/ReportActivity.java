package fm.doe.national.report.ui.report;

import android.os.Bundle;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.report.R;
import fm.doe.national.report.di.ComponentInjector;
import fm.doe.national.report_core.model.ReportPage;
import fm.doe.national.report_core.ui.summary_header.SummaryHeaderView;

public class ReportActivity extends BaseActivity implements ReportView {

    @InjectPresenter
    ReportPresenter presenter;

    private final ReportTabsPagerAdapter tabsPagerAdapter = new ReportTabsPagerAdapter(this);

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView schoolIdTextView;
    private TextView visitDateTextView;
    private TextView schoolNameTextView;
    private TextView principalNameTextView;

    @ProvidePresenter
    ReportPresenter providePresenter() {
        return new ReportPresenter(ComponentInjector.getComponent(getApplication()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        setupTabs();
    }

    private void setupTabs() {
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void bindViews() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        schoolIdTextView = findViewById(R.id.textview_school_code);
        visitDateTextView = findViewById(R.id.textview_visit_date);
        schoolNameTextView = findViewById(R.id.textview_school_name);
        principalNameTextView = findViewById(R.id.textview_principal_name);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }

    @Override
    public void setReportPages(List<ReportPage> pages) {
        tabsPagerAdapter.setFragments(pages.stream().map(ReportPage::buildFragment).collect(Collectors.toList()));
    }

    public void setHeaderItem(SummaryHeaderView.Item item) {
        schoolIdTextView.setText(item.getSchoolId());
        schoolNameTextView.setText(item.getSchoolName());
        principalNameTextView.setText(item.getPrincipalName());
        visitDateTextView.setText(DateUtils.formatUi(item.getDate()));
    }
}
