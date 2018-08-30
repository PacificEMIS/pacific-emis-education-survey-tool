package fm.doe.national.ui.screens.standard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.utils.ViewUtils;

public class StandardActivity extends BaseActivity implements StandardView {

    private static final String EXTRA_ACCREDITATION = "EXTRA_ACCREDITATION";
    private static final String EXTRA_STANDARD = "EXTRA_STANDARD";
    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";

    private static final int[] icons = {
            R.drawable.ic_standard_leadership_selector,
            R.drawable.ic_standard_teacher_selector,
            R.drawable.ic_standard_data_selector,
            R.drawable.ic_standard_cirriculum_selector,
            R.drawable.ic_standard_facility_selector,
            R.drawable.ic_standard_improvement_selector,
            R.drawable.ic_standard_observation_selector
    };

    private final CriteriaListAdapter recyclerAdapter = new CriteriaListAdapter();

    @BindView(R.id.recyclerview_criterias)
    RecyclerView criteriasRecycler;

    @BindView(R.id.textview_standard_name)
    TextView titleTextView;

    @BindView(R.id.textview_standard_progress)
    TextView progressTextView;

    @BindView(R.id.imageview_standard_icon)
    ImageView iconImageView;

    @BindView(R.id.constraintlayout_prev)
    View prevStandardView;

    @BindView(R.id.constraintlayout_next)
    View nextStandardView;

    @BindView(R.id.imageview_standard_icon_prev)
    ImageView iconPrevStandardImageView;

    @BindView(R.id.imageview_standard_icon_next)
    ImageView iconNextStandardImageView;

    @BindView(R.id.textview_standard_title_prev)
    TextView prevStandardTitleTextView;

    @BindView(R.id.textview_standard_title_next)
    TextView nextStandardTitleTextView;

    @InjectPresenter
    StandardPresenter presenter;

    @ProvidePresenter
    public StandardPresenter providePresenter() {
        return new StandardPresenter(
                getIntent().getLongExtra(EXTRA_ACCREDITATION, -1),
                getIntent().getLongExtra(EXTRA_STANDARD, -1),
                getIntent().getLongArrayExtra(EXTRA_GROUPS));
    }

    @NonNull
    public static Intent createIntent(@NonNull Context context, long passingId, long standardId, Long[] groupIds) {
        return new Intent(context, StandardActivity.class)
                .putExtra(EXTRA_ACCREDITATION, passingId)
                .putExtra(EXTRA_STANDARD, standardId)
                .putExtra(EXTRA_GROUPS, groupIds);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        criteriasRecycler.setAdapter(recyclerAdapter);
        recyclerAdapter.subscribeOnChanges(presenter::onSubCriteriaStateChanged);

        prevStandardView.setOnClickListener((View v) -> presenter.onPreviousPressed());
        nextStandardView.setOnClickListener((View v) -> presenter.onNextPressed());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_standard;
    }

    @Override
    public void setCriterias(@NonNull List<Criteria> criterias) {
        recyclerAdapter.setItems(criterias);
    }

    @Override
    public void setGlobalInfo(String title, int resourceIndex) {
        titleTextView.setText(title);
        applyIcon(iconImageView, resourceIndex, false);
    }

    @Override
    public void setProgress(int answered, int total) {
        progressTextView.setText(getString(R.string.criteria_progress, answered, total));
    }

    @Override
    public void setPrevStandard(String title, int resourceIndex) {
        prevStandardTitleTextView.setText(title);
        applyIcon(iconPrevStandardImageView, resourceIndex, true);
    }

    @Override
    public void setNextStandard(String title, int resourceIndex) {
        nextStandardTitleTextView.setText(title);
        applyIcon(iconNextStandardImageView, resourceIndex, true);
    }

    private void applyIcon(ImageView imageView, int forIndex, boolean isHighlighted) {
        if (forIndex >= ViewUtils.STANDARD_ICONS.length) return;

        Drawable drawable = getResources().getDrawable(ViewUtils.STANDARD_ICONS[forIndex]);
        imageView.setImageDrawable(drawable);
        imageView.setActivated(isHighlighted);
    }
}
