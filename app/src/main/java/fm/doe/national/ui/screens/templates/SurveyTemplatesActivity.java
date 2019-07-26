package fm.doe.national.ui.screens.templates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.templates.accreditation.AccreditationSurveyTemplateFragment;
import fm.doe.national.ui.screens.templates.wash.WashSurveyTemplateFragment;

public class SurveyTemplatesActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private final SurveyTemplatesFragmentStatePagerAdapter tabsPagerAdapter =
            new SurveyTemplatesFragmentStatePagerAdapter(this);

    public static Intent createIntent(Context context) {
        return new Intent(context, SurveyTemplatesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_templates);
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabsPagerAdapter.setFragments(Arrays.asList(
                AccreditationSurveyTemplateFragment.create(),
                WashSurveyTemplateFragment.create()
        ));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_survey_templates;
    }
}
