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
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.adapters.CriteriaAdapter;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.view_data.CriteriaViewData;

public class StandardActivity extends BaseActivity implements StandardView {

    private static final String ACCREDITATION_EXTRA = "ACCREDITATION_EXTRA";
    private static int[] icons = {
            R.drawable.ic_standard_leadership_selector,
            R.drawable.ic_standard_teacher_selector,
            R.drawable.ic_standard_data_selector,
            R.drawable.ic_standard_cirriculum_selector,
            R.drawable.ic_standard_facility_selector,
            R.drawable.ic_standard_improvement_selector,
            R.drawable.ic_standard_observation_selector
    };

    @NonNull
    public static Intent createIntent(@NonNull Context context, @NonNull SchoolAccreditationPassing accreditationResult) {
        return new Intent(context, StandardActivity.class)
                .putExtra(ACCREDITATION_EXTRA, accreditationResult);
    }

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
        return new StandardPresenter(getSerializableArgument(ACCREDITATION_EXTRA, SchoolAccreditationPassing.class));
    }

    private CriteriaAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        recyclerAdapter = new CriteriaAdapter();
        criteriasRecycler.setAdapter(recyclerAdapter);
        recyclerAdapter.subscribeOnChanges(presenter::onQuestionStateChanged);

        prevStandardView.setOnClickListener((View v) -> presenter.onPreviousPressed());
        nextStandardView.setOnClickListener((View v) -> presenter.onNextPressed());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_standard;
    }

    @Override
    public void setCriterias(@NonNull List<CriteriaViewData> criterias) {
        recyclerAdapter.setCriterias(criterias);
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
        if (forIndex >= icons.length) return;

        Drawable drawable = getResources().getDrawable(icons[forIndex]);
        if (isHighlighted) drawable.setState(new int[] { R.attr.highlight });
        imageView.setImageDrawable(drawable);
    }
}
