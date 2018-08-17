package fm.doe.national.ui.screens.standard;

import android.app.Activity;
import android.content.Intent;
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
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.adapters.CriteriaAdapter;
import fm.doe.national.ui.screens.base.BaseActivity;

public class StandardActivity extends BaseActivity implements StandardView {

    public static final String STANDARD_EXTRA = "STANDARD_EXTRA";

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
    public static Intent getStartingIntent(@NonNull Activity parent, @NonNull MockStandard forStandard) {
        Intent intent = new Intent(parent, StandardActivity.class);
        intent.putExtra(STANDARD_EXTRA, forStandard);
        return intent;
    }

    //region Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        parseStartingBundle();
    }

    //endregion

    //region StandardViewImpl

    @Override
    public void bindCriterias(@NonNull List<MockCriteria> criterias) {
        recyclerAdapter.setCriterias(criterias);
    }

    @Override
    public void bindGlobalInfo(String title, int icon) {
        titleTextView.setText(title);
        iconImageView.setImageResource(icon);
    }

    @Override
    public void bindProgress(int answered, int total) {
        progressTextView.setText(String.format(Locale.US, "%d/%d", answered, total));
    }

    @Override
    public void bindPrevStandard(String title, int icon) {
        prevStandardTitleTextView.setText(title);
        iconPrevStandardImageView.setImageResource(icon);
    }

    @Override
    public void bindNextStandard(String title, int icon) {
        nextStandardTitleTextView.setText(title);
        iconNextStandardImageView.setImageResource(icon);
    }

    @Override
    public void navigateToOtherStandard(MockStandard otherStandard) {
        startActivity(StandardActivity.getStartingIntent(this, otherStandard));
        finish();
    }

    //endregion

    private void parseStartingBundle() {
        try {
            presenter.setStandard((MockStandard) getIntent().getSerializableExtra(STANDARD_EXTRA));
        } catch (NullPointerException npe) {
            // TODO: uncomment after ui merge
//            throw new RuntimeException(
//                    "StandardActivity should be started with intent created by StandardActivity.getStartingIntent(...)");
            // TODO: remove after ui merge
            presenter.setStandard(new MockStandard());
        }
    }

    private void setupViews() {
        setContentView(R.layout.activity_standard);
        ButterKnife.bind(this);
        initToolbar();

        recyclerAdapter = new CriteriaAdapter();
        criteriasRecycler.setAdapter(recyclerAdapter);
        recyclerAdapter.subscribeOnChanges(() -> presenter.onQuestionStateChanged());

        prevStandardView.setOnClickListener((View v) -> presenter.onPreviousPressed());
        nextStandardView.setOnClickListener((View v) -> presenter.onNextPressed());
    }
}
