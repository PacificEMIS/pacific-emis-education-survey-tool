package org.pacific_emis.surveys.ui.screens.templates;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.Collections;
import java.util.List;

import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;

public class SurveyTemplatesFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<SurveyTemplateFragment> fragments = Collections.emptyList();
    private Context context;

    public SurveyTemplatesFragmentStatePagerAdapter(BaseActivity activity) {
        super(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = activity;
    }

    public void setFragments(List<SurveyTemplateFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SurveyTemplateFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getPageTitle().getCharSequence(context);
    }
}