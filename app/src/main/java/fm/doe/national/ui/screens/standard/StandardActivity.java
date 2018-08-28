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
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.view_data.CriteriaViewData;
import fm.doe.national.utils.ViewUtils;

public class StandardActivity extends BaseActivity implements StandardView {

    private static final String EXTRA_ACCREDITATION = "EXTRA_ACCREDITATION";

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

    private final CriteriaAdapter recyclerAdapter = new CriteriaAdapter();

    @InjectPresenter
    StandardPresenter presenter;

    @ProvidePresenter
    public StandardPresenter providePresenter() {
        return new StandardPresenter(getIntent().getLongExtra(EXTRA_ACCREDITATION, -1));
    }

    @NonNull
    public static Intent createIntent(@NonNull Context context, long passingId) {
        return new Intent(context, StandardActivity.class)
                .putExtra(EXTRA_ACCREDITATION, passingId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
