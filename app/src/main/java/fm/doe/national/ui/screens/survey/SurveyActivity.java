package fm.doe.national.ui.screens.survey;

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

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.survey.navigation.NavigationItem;

public class SurveyActivity extends BaseActivity implements SurveyView, BaseAdapter.OnItemClickListener<NavigationItem> {

    @BindView(R.id.omagetextview_current_title)
    OmegaTextView navigationTitleOmegaTextView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @BindView(R.id.imageview_expanding_arrow)
    ImageView arrowImageView;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @InjectPresenter
    SurveyPresenter presenter;

    private final NavigationItemsAdapter navigationItemsAdapter = new NavigationItemsAdapter(this);

    private boolean isNavigatorOpened;

    @Override
    protected int getContentView() {
        return R.layout.activity_survey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setAdapter(navigationItemsAdapter);
    }

    @Override
    public void setSchoolName(String schoolName) {
        setTitle(schoolName);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setNavigationTitle(@Nullable Text prefix, Text name, @Nullable Progress progress) {
        navigationTitleOmegaTextView.setStartText(prefix);
        navigationTitleOmegaTextView.setText(name);
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

    @OnClick(R.id.layout_navigator)
    void onNavigatioPressed() {
        toggleNavigator();
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
