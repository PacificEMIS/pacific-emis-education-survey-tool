package org.pacific_emis.surveys.report.ui.report;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.Collections;
import java.util.List;

import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportFragment;

public class ReportTabsPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseReportFragment> fragments = Collections.emptyList();
    private Context context;

    public ReportTabsPagerAdapter(BaseFragment fragment) {
        super(fragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = fragment.getContext();
    }

    public void setFragments(List<BaseReportFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseReportFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getTabName().getString(context);
    }
}
