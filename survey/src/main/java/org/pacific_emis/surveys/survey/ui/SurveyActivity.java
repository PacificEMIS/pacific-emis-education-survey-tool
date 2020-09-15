package org.pacific_emis.surveys.survey.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.views.OmegaTextView;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import org.pacific_emis.surveys.accreditation.ui.survey.AccreditationSurveyView;
import org.pacific_emis.surveys.accreditationSurvey.R;
import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.survey.di.SurveyComponentInjector;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItemsAdapter;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyPresenter;
import org.pacific_emis.surveys.wash.ui.survey.WashSurveyView;

import java.util.List;

public class SurveyActivity extends BaseActivity implements
        AccreditationSurveyView,
        WashSurveyView,
        BaseAdapter.OnItemClickListener<NavigationItem>,
        View.OnClickListener {

    private static final long NAVIGATOR_ANIMATION_DURATION = 500L;

    private final NavigationItemsAdapter navigationItemsAdapter = new NavigationItemsAdapter(this);

    private OmegaTextView navigationTitleTextView;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private ImageView arrowImageView;
    private RecyclerView recyclerView;
    private View navigatorHeaderView;
    private boolean isNavigatorOpened;

    @InjectPresenter
    SurveyPresenter presenter;

    @ProvidePresenter
    SurveyPresenter providePresenter() {
        return SurveyComponentInjector.getComponent(getApplication()).getSurveyPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        recyclerView.setAdapter(navigationItemsAdapter);
        navigatorHeaderView.setOnClickListener(this);
    }

    private void bindViews() {
        navigationTitleTextView = findViewById(R.id.omagetextview_current_title);
        progressBar = findViewById(R.id.progressbar);
        progressTextView = findViewById(R.id.textview_progress);
        arrowImageView = findViewById(R.id.imageview_expanding_arrow);
        recyclerView = findViewById(R.id.recyclerview);
        navigatorHeaderView = findViewById(R.id.layout_navigator);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_survey;
    }

    @Override
    public void setSchoolName(String schoolName) {
        setTitle(schoolName);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setNavigationTitle(@Nullable Text prefix, Text name, @Nullable Progress progress) {
        navigationTitleTextView.setText(name);
        navigationTitleTextView.setStartText(prefix);
        navigationTitleTextView.setStartSpaceText(prefix == null ? null : Text.from(" "));

        if (progress != null) {
            progressTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            ViewUtils.rebindProgress(progress, progressTextView, progressBar);
        } else {
            progressTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        closeNavigator();
    }

    @Override
    public void setNavigationItems(List<NavigationItem> items) {
        navigationItemsAdapter.setItems(items);
    }

    @Override
    public void showNavigationItem(BuildableNavigationItem item) {
        navigationItemsAdapter.setSelectedItem(item.getId());
        replaceFragment(R.id.layout_fragment_container, item.buildFragment());
    }

    @Override
    public void onItemClick(NavigationItem item) {
        presenter.onNavigationItemPressed((BuildableNavigationItem) item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_navigator) {
            toggleNavigator();
        }
    }

    private void toggleNavigator() {
        isNavigatorOpened = !isNavigatorOpened;
        if (isNavigatorOpened) {
            final View focusedView = getCurrentFocus();
            if (focusedView != null) {
                ViewUtils.hideKeyboardAndClearFocus(focusedView, findViewById(R.id.layout_navigator));
            }
        }
        onNavigatorStateChanged();
    }

    private void closeNavigator() {
        if (isNavigatorOpened) {
            toggleNavigator();
        }
    }

    private void onNavigatorStateChanged() {
        if (isNavigatorOpened) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.animate()
                    .translationY(0)
                    .setDuration(NAVIGATOR_ANIMATION_DURATION)
                    .setListener(null)
                    .start();
            arrowImageView.setImageResource(R.drawable.ic_expand_less_24dp);
        } else {
            recyclerView.animate()
                    .translationY(-recyclerView.getHeight())
                    .setDuration(NAVIGATOR_ANIMATION_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            recyclerView.setVisibility(View.GONE);
                        }
                    })
                    .start();
            arrowImageView.setImageResource(R.drawable.ic_expand_more_24dp);
        }
    }

    @Override
    public void updateQuestionsGroupProgress(long id, Progress progress) {
        ViewUtils.rebindProgress(progress, progressTextView, progressBar);
        navigationItemsAdapter.notifyProgressChanged(id, progress);
    }

    @Override
    public void setReportEnabled(boolean enabled) {
        navigationItemsAdapter.setReportEnabled(enabled);
    }

    @Override
    public void close() {
        this.finish();
    }
}
