package fm.doe.national.ui.screens.report;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.omegar.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.fcm_report.ui.levels.LevelsFragment;
import fm.doe.national.ui.screens.report.recommendations.RecommendationsFragment;
import fm.doe.national.core.ui.screens.report.summary.SummaryFragment;

public class ReportActivity extends BaseActivity implements ReportView {

    @InjectPresenter
    ReportPresenter presenter;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private final ReportTabsPagerAdapter tabsPagerAdapter = new ReportTabsPagerAdapter(
            this,
            new SummaryFragment(),
            new RecommendationsFragment(),
            new LevelsFragment()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }

}
