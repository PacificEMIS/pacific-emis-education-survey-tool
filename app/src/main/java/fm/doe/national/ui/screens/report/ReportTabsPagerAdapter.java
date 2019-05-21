package fm.doe.national.ui.screens.report;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.report.base.BaseReportFragment;

public class ReportTabsPagerAdapter extends FragmentStatePagerAdapter {

    private final List<BaseReportFragment> fragments;
    private Context context;

    public ReportTabsPagerAdapter(BaseActivity activity, BaseReportFragment... fragments) {
        super(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = Arrays.asList(fragments);
        this.context = activity;
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
