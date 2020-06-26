package org.pacific_emis.surveys.report.ui.report;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.report.R;
import org.pacific_emis.surveys.report.di.ReportComponentInjector;
import org.pacific_emis.surveys.report_core.model.ReportPage;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;

public class ReportFragment extends BaseFragment implements ReportView {

    @InjectPresenter
    ReportPresenter presenter;

    private ReportTabsPagerAdapter tabsPagerAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView schoolIdTextView;
    private TextView visitDateTextView;
    private TextView schoolNameTextView;
    private TextView principalNameTextView;

    @ProvidePresenter
    ReportPresenter providePresenter() {
        Application application = getActivity().getApplication();
        return new ReportPresenter(
                ReportComponentInjector.getComponent(application)
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupTabs();
    }

    private void bindViews(View v) {
        tabLayout = v.findViewById(R.id.tablayout);
        viewPager = v.findViewById(R.id.viewpager);
        schoolIdTextView = v.findViewById(R.id.textview_school_code);
        visitDateTextView = v.findViewById(R.id.textview_visit_date);
        schoolNameTextView = v.findViewById(R.id.textview_school_name);
        principalNameTextView = v.findViewById(R.id.textview_principal_name);
    }

    private void setupTabs() {
        tabsPagerAdapter = new ReportTabsPagerAdapter(this);
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setReportPages(List<ReportPage> pages) {
        tabsPagerAdapter.setFragments(pages.stream().map(ReportPage::buildFragment).collect(Collectors.toList()));
    }

    public void setHeaderItem(LevelLegendView.Item item) {
        schoolIdTextView.setText(item.getSchoolId());
        schoolNameTextView.setText(item.getSchoolName());
        principalNameTextView.setText(item.getPrincipalName());
        visitDateTextView.setText(item.getSurveyTag());
    }

}
