package fm.doe.national.ui.screens.standard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.ui.adapters.CriteriaAdapter;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.view_data.CriteriaViewData;

public class StandardActivity extends BaseActivity implements StandardView {

    public static final String ACCREDITATION_EXTRA = "ACCREDITATION_EXTRA";

    @BindView(R.id.recyclerview_criterias)
    RecyclerView criteriasRecycler;

    @BindView(R.id.textview_standard_name)
    TextView titleTextView;

    @BindView(R.id.textview_standard_progress)
    TextView progressTextView;

    @BindView(R.id.image_standard_icon)
    ImageView iconImageView;

    @BindView(R.id.constraintlayout_prev)
    View prevStandardView;

    @BindView(R.id.constraintlayout_next)
    View nextStandardView;

    @BindView(R.id.image_standard_icon_prev)
    ImageView iconPrevStandardImageView;

    @BindView(R.id.image_standard_icon_next)
    ImageView iconNextStandardImageView;

    @BindView(R.id.textview_standard_title_prev)
    TextView prevStandardTitleTextView;

    @BindView(R.id.textview_standard_title_next)
    TextView nextStandardTitleTextView;

    @InjectPresenter
    StandardPresenter presenter;

    private CriteriaAdapter recyclerAdapter;

    @NonNull
    public static Intent createIntent(@NonNull Activity parent, @NonNull SchoolAccreditationResult accreditationResult) {
        return new Intent(parent, StandardActivity.class)
                .putExtra(ACCREDITATION_EXTRA, accreditationResult);
    }

    //region icons static
    private static int[] icons = {
            R.drawable.ic_standard_leadership_selector,
            R.drawable.ic_standard_teacher_selector,
            R.drawable.ic_standard_data_selector,
            R.drawable.ic_standard_cirriculum_selector,
            R.drawable.ic_standard_facility_selector,
            R.drawable.ic_standard_improvement_selector,
            R.drawable.ic_standard_observation_selector
    };
    //endregion

    //region Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        parseStartingBundle();
    }

    //endregion

    @Override
    protected int getContentView() {
        return R.layout.activity_standard;
    }

    //region StandardViewImpl

    @Override
    public void bindCriterias(@NonNull List<CriteriaViewData> criterias) {
        recyclerAdapter.setCriterias(criterias);
    }

    @Override
    public void bindGlobalInfo(String title, int index) {
        titleTextView.setText(title);
        applyIcon(iconImageView, index, false);
    }

    @Override
    public void bindProgress(int answered, int total) {
        progressTextView.setText(String.format(Locale.US, "%d/%d", answered, total));
    }

    @Override
    public void bindPrevStandard(String title, int index) {
        prevStandardTitleTextView.setText(title);
        applyIcon(iconPrevStandardImageView, index, true);
    }

    @Override
    public void bindNextStandard(String title, int index) {
        nextStandardTitleTextView.setText(title);
        applyIcon(iconNextStandardImageView, index, true);
    }

    //endregion

    private void parseStartingBundle() {
        try {
            presenter.setAccreditationResult((SchoolAccreditationResult) getIntent().getSerializableExtra(ACCREDITATION_EXTRA));
        } catch (NullPointerException | ClassCastException ex) {
            throw new RuntimeException(
                    "StandardActivity should be started with intent created by StandardActivity.getStartingIntent(...)");
        }
    }

    private void setupViews() {
        recyclerAdapter = new CriteriaAdapter();
        criteriasRecycler.setAdapter(recyclerAdapter);
        recyclerAdapter.subscribeOnChanges(presenter::onQuestionStateChanged);

        prevStandardView.setOnClickListener((View v) -> presenter.onPreviousPressed());
        nextStandardView.setOnClickListener((View v) -> presenter.onNextPressed());
    }

    private void applyIcon(ImageView imageView, int forIndex, boolean isHighlighted) {
        if (forIndex >= icons.length) return;

        Drawable drawable = getResources().getDrawable(icons[forIndex]);
        if (isHighlighted) drawable.setState(new int[] { R.attr.highlight });
        imageView.setImageDrawable(drawable);
    }
}
