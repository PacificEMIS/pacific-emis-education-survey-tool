package fm.doe.national.core.ui.screens.survey;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;

import fm.doe.national.core.R;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.di.ComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.ui.screens.survey.navigation.NavigationItem;
import fm.doe.national.core.utils.TextUtil;
import fm.doe.national.core.utils.ViewUtils;

public class SurveyActivity extends BaseActivity implements
        SurveyView,
        BaseAdapter.OnItemClickListener<NavigationItem>,
        View.OnClickListener {

    @InjectPresenter
    SurveyPresenter presenter;

    @ProvidePresenter
    SurveyPresenter providePresenter() {
        return new SurveyPresenter(ComponentInjector.getComponent(getApplication()));
    }

    private final NavigationItemsAdapter navigationItemsAdapter = new NavigationItemsAdapter(this);

    private Typeface navigationPrefixTypeface;
    private TextView navigationTitleTextView;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private ImageView arrowImageView;
    private RecyclerView recyclerView;
    private View navigatorHeaderView;
    private boolean isNavigatorOpened;

    @Override
    protected int getContentView() {
        return R.layout.activity_survey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationPrefixTypeface = ResourcesCompat.getFont(this, R.font.noto_sans_bold);
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
    public void setSchoolName(String schoolName) {
        setTitle(schoolName);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setNavigationTitle(@Nullable Text prefix, Text name, @Nullable Progress progress) {

        if (prefix == null) {
            name.applyTo(navigationTitleTextView, null);
        } else {
            navigationTitleTextView.setText(TextUtil.createSpannableString(
                    this,
                    prefix,
                    Text.from(" "),
                    name,
                    navigationPrefixTypeface
            ));
        }

        if (progress != null) {
            progressTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            ViewUtils.rebindProgress(progress, progressTextView, progressBar);
        } else {
            progressTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setNavigationItems(List<NavigationItem> items) {
        navigationItemsAdapter.setItems(items);
    }

    @Override
    public void showNavigationItem(NavigationItem item) {
        replaceFragment(R.id.layout_fragment_container, item.buildFragment());
    }

    @Override
    public void onItemClick(NavigationItem item) {
        presenter.onNavigationItemPressed(item);
        toggleNavigator();
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

    private void onNavigatorStateChanged() {
        if (isNavigatorOpened) {
            recyclerView.setVisibility(View.VISIBLE);
            arrowImageView.setImageResource(R.drawable.ic_expand_less_24dp);
        } else {
            recyclerView.setVisibility(View.GONE);
            arrowImageView.setImageResource(R.drawable.ic_expand_more_24dp);
        }
    }
}
