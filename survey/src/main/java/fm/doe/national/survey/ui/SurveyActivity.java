package fm.doe.national.survey.ui;

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

import java.util.List;

import fm.doe.national.accreditationSurvey.R;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.survey.di.SurveyComponentInjector;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItemsAdapter;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;
import fm.doe.national.survey_core.ui.survey.SurveyView;

public class SurveyActivity extends BaseActivity implements
        SurveyView,
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
        presenter.onNavigationItemPressed((BuildableNavigationItem)item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_navigator) {
            toggleNavigator();
        }
    }

    private void toggleNavigator() {
        isNavigatorOpened = !isNavigatorOpened;
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
