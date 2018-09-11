package fm.doe.national.ui.screens.standard;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.omega_r.libs.omegatypes.Text;

import java.io.File;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.utils.Constants;
import fm.doe.national.utils.ViewUtils;

public class StandardActivity extends BaseActivity implements
        StandardView,
        SubcriteriaCallback,
        CommentDialogFragment.OnCommentSubmitListener {

    private static final String EXTRA_ACCREDITATION = "EXTRA_ACCREDITATION";
    private static final String EXTRA_STANDARD = "EXTRA_STANDARD";
    private final static String TAG_DIALOG = "TAG_DIALOG";
    private static final int REQUEST_CAMERA = 100;

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

    @BindView(R.id.layout_container)
    View topContainerView;

    @BindView(R.id.imageview_expand_container)
    ImageView expandContainerImageView;

    @InjectPresenter
    StandardPresenter presenter;

    @BindInt(android.R.integer.config_shortAnimTime)
    int shortAnimationDuration;

    @ProvidePresenter
    public StandardPresenter providePresenter() {
        return new StandardPresenter(
                getIntent().getLongExtra(EXTRA_ACCREDITATION, -1),
                getIntent().getLongExtra(EXTRA_STANDARD, -1));
    }

    @NonNull
    public static Intent createIntent(@NonNull Context context, long passingId, long standardId) {
        return new Intent(context, StandardActivity.class)
                .putExtra(EXTRA_ACCREDITATION, passingId)
                .putExtra(EXTRA_STANDARD, standardId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    presenter.onTakePhotoSuccess();
                } else {
                    presenter.onTakePhotoFailure();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initViews() {
        setToolbarMode(ToolbarDisplaying.SECONDARY);

        criteriasRecycler.setAdapter(recyclerAdapter);
        recyclerAdapter.setCallback(this);

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

    @Override
    public void setSurveyYear(int year) {
        setToolbarYear(year);
    }

    @Override
    public void setSchoolName(String schoolName) {
        setTitle(schoolName);
    }

    @Override
    public void onSubCriteriaStateChanged(@NonNull SubCriteria subCriteria, Answer.State previousState) {
        presenter.onSubCriteriaStateChanged(subCriteria, previousState);
    }

    @Override
    public void onEditCommentClicked(SubCriteria subCriteria) {
        presenter.onEditCommentClicked(subCriteria);
    }

    @Override
    public void onAddCommentClicked(SubCriteria subCriteria) {
        presenter.onAddCommentClicked(subCriteria);
    }

    @Override
    public void onRemoveCommentClicked(SubCriteria subCriteria) {
        presenter.onDeleteCommentClicked(subCriteria);
    }

    @Override
    public void onAddPhotoClicked(SubCriteria subCriteria) {
        presenter.onAddPhotoClicked(subCriteria);
    }

    @Override
    public void onRemovePhotoClicked(SubCriteria subCriteria, String photoPath) {
        presenter.onDeletePhotoClicked(subCriteria, photoPath);
    }

    @Override
    public void takePictureTo(@NonNull File toFile) {
        PackageManager pm = getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            showToast(Text.from(R.string.error_take_picture));
            presenter.onTakePhotoFailure();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(pm) != null) {
            Uri photoURI = FileProvider.getUriForFile(this, Constants.AUTHORITY_FILE_PROVIDER, toFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onPhotoClicked(View anchor, String photoPath) {
        Intent intent = FullscreenImageActivity.createIntent(this, photoPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(anchor, FullscreenImageActivity.TRANSITION_IMAGE);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, anchor, FullscreenImageActivity.TRANSITION_IMAGE);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    @Override
    public void notifySubCriteriaChanged(SubCriteria subCriteria) {
        recyclerAdapter.notify(subCriteria);
    }

    @Override
    public void showCommentEditor(SubCriteria subCriteria) {
        CommentDialogFragment dialog = CommentDialogFragment.create(subCriteria);
        dialog.show(getSupportFragmentManager(), TAG_DIALOG);
    }

    @Override
    public void onCommentSubmit(String comment) {
        presenter.onCommentEdit(comment);
    }

    private void applyIcon(ImageView imageView, int forIndex, boolean isHighlighted) {
        if (forIndex >= ViewUtils.STANDARD_ICONS.length) return;

        Drawable drawable = getResources().getDrawable(ViewUtils.STANDARD_ICONS[forIndex]);
        imageView.setImageDrawable(drawable);
        imageView.setActivated(isHighlighted);
    }
}
