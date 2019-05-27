package fm.doe.national.report.ui.report;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.report.R;
import fm.doe.national.report.di.ComponentInjector;
import fm.doe.national.report_core.model.ReportPage;

public class ReportActivity extends BaseActivity implements ReportView {

    @InjectPresenter
    ReportPresenter presenter;

    TabLayout tabLayout;

    ViewPager viewPager;

    private final ReportTabsPagerAdapter tabsPagerAdapter = new ReportTabsPagerAdapter(this);

    @ProvidePresenter
    ReportPresenter providePresenter() {
        return new ReportPresenter(ComponentInjector.getComponent(getApplication()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void bindViews() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }

    @Override
    public void setReportPages(List<ReportPage> pages) {
        tabsPagerAdapter.setFragments(pages.stream().map(ReportPage::buildFragment).collect(Collectors.toList()));
    }
}
