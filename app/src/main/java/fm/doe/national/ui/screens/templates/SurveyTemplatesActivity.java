package fm.doe.national.ui.screens.templates;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;

public class SurveyTemplatesActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private final SurveyTemplatesFragmentStatePagerAdapter tabsPagerAdapter = new SurveyTemplatesFragmentStatePagerAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_survey_templates;
    }
}
