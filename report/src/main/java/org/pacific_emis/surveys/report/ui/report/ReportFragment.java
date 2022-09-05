package org.pacific_emis.surveys.report.ui.report;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponentInjector;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.di.CoreComponentInjector;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.core.utils.DebounceTextChangedWatcher;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.report.R;
import org.pacific_emis.surveys.report.di.ReportComponentInjector;
import org.pacific_emis.surveys.report_core.model.ReportPage;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;

public class ReportFragment extends BaseFragment implements ReportView, AdapterView.OnItemClickListener {

    private static final long DELAY_CHANGED_MILLIS = 1000;

    @InjectPresenter
    ReportPresenter presenter;

    private ReportTabsPagerAdapter tabsPagerAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView schoolIdTextView;
    private TextView visitDateTextView;
    private TextView schoolNameTextView;
    private AutoCompleteTextView principalNameTextView;

    private View rootView;
    private ArrayAdapter<Teacher> adapter;

    @ProvidePresenter
    ReportPresenter providePresenter() {
        Application application = getActivity().getApplication();
        return new ReportPresenter(
                ReportComponentInjector.getComponent(application),
                CoreComponentInjector.getComponent(application),
                AccreditationCoreComponentInjector.getComponent(application)
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
        DebounceTextChangedWatcher.setup(
                principalNameTextView,
                DELAY_CHANGED_MILLIS,
                newText -> presenter.onPrincipalNameChanges(newText)
        );
    }

    private void bindViews(View v) {
        tabLayout = v.findViewById(R.id.tablayout);
        viewPager = v.findViewById(R.id.viewpager);
        schoolIdTextView = v.findViewById(R.id.textview_school_code);
        visitDateTextView = v.findViewById(R.id.textview_visit_date);
        schoolNameTextView = v.findViewById(R.id.textview_school_name);
        principalNameTextView = v.findViewById(R.id.textview_principal_name);
        rootView = v.findViewById(R.id.layout_root);
        adapter =  new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
    }

    private void setupTabs() {
        tabsPagerAdapter = new ReportTabsPagerAdapter(this);
        principalNameTextView.setOnItemClickListener(this);
        principalNameTextView.setAdapter(adapter);
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

    @Override
    public void setPrincipalToAutocompleteField(@NonNull List<Teacher> teachers) {
        adapter.clear();
        adapter.addAll(teachers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewUtils.hideKeyboardAndClearFocus(principalNameTextView, rootView);
        presenter.onPrincipalNameChanges(adapter.getItem(position));
    }
}
