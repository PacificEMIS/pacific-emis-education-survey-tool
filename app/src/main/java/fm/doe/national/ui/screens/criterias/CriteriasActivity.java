package fm.doe.national.ui.screens.criterias;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableSubCriteria;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.photos.PhotosActivity;
import fm.doe.national.utils.Constants;
import fm.doe.national.utils.DateUtils;
import fm.doe.national.utils.ViewUtils;

public class CriteriasActivity extends BaseActivity implements
        CriteriasView,
        SubcriteriaCallback,
        CommentDialogFragment.OnCommentSubmitListener {

    private static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    private static final String EXTRA_STANDARD = "EXTRA_STANDARD";
    private final static String TAG_DIALOG = "TAG_DIALOG";
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_CHANGES = 101;

    private final CriteriaListAdapter recyclerAdapter = new CriteriaListAdapter();

    @BindView(R.id.recyclerview_criterias)
    RecyclerView criteriasRecycler;

    @BindView(R.id.textview_standard_name)
    TextView titleTextView;

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

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

    @BindView(R.id.textview_year)
    TextView yearTextView;

    @BindView(R.id.textview_category_name)
    TextView categoryNameTextView;

    @InjectPresenter
    CriteriasPresenter presenter;

    @BindInt(android.R.integer.config_shortAnimTime)
    int shortAnimationDuration;

    @ProvidePresenter
    public CriteriasPresenter providePresenter() {
        Intent intent = getIntent();
        return new CriteriasPresenter(
                intent.getLongExtra(EXTRA_CATEGORY, -1),
                intent.getLongExtra(EXTRA_STANDARD, -1));
    }

    @NonNull
    public static Intent createIntent(@NonNull Context context, long categoryId, long standardId) {
        return new Intent(context, CriteriasActivity.class)
                .putExtra(EXTRA_CATEGORY, categoryId)
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
            case REQUEST_CHANGES:
                presenter.onReturnedFromMorePhotos();
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initViews() {
        setToolbarMode(ToolbarDisplaying.PRIMARY);

        recyclerAdapter.setCallback(this);

        prevStandardView.setOnClickListener(v -> presenter.onPreviousPressed());
        nextStandardView.setOnClickListener(v -> presenter.onNextPressed());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_criterias;
    }

    @Override
    public void setCriterias(@NonNull List<MutableCriteria> criterias) {
        recyclerAdapter.setItems(criterias);

        // Fix for bug #55747 The following category is displayed from the end
        //
        // need to reset recycler adapter when changing criteria to collapse everything and scroll to top
        criteriasRecycler.setAdapter(recyclerAdapter);
    }

    @Override
    public void setGlobalInfo(String title, @Nullable String resourceIndex) {
        setTitle(title);
        applyIcon(iconImageView, ViewUtils.getStandardIconRes(resourceIndex), false);
    }

    @Override
    public void setProgress(int answered, int total) {
        ViewUtils.rebindProgress(total, answered, getString(R.string.criteria_progress), progressTextView, progressBar);
    }

    @Override
    public void setPrevStandard(String title, @Nullable String resourceIndex) {
        prevStandardTitleTextView.setText(title);
        applyIcon(iconPrevStandardImageView, ViewUtils.getStandardIconRes(resourceIndex), true);
    }

    @Override
    public void setNextStandard(String title, @Nullable String resourceIndex) {
        nextStandardTitleTextView.setText(title);
        applyIcon(iconNextStandardImageView, ViewUtils.getStandardIconRes(resourceIndex), true);
    }

    @Override
    public void setSurveyDate(Date date) {
        yearTextView.setText(DateUtils.formatMonthYear(date));
    }

    @Override
    public void setSchoolName(String schoolName) {
        titleTextView.setText(schoolName);
    }

    @Override
    public void setCategoryName(String categoryName) {
        categoryNameTextView.setText(categoryName);
    }

    @Override
    public void onSubCriteriaStateChanged(@NonNull MutableSubCriteria subCriteria) {
        presenter.onSubCriteriaStateChanged(subCriteria);
    }

    @Override
    public void onEditCommentClicked(MutableSubCriteria subCriteria) {
        presenter.onEditCommentClicked(subCriteria);
    }

    @Override
    public void onAddCommentClicked(MutableSubCriteria subCriteria) {
        presenter.onAddCommentClicked(subCriteria);
    }

    @Override
    public void onRemoveCommentClicked(MutableSubCriteria subCriteria) {
        presenter.onDeleteCommentClicked(subCriteria);
    }

    @Override
    public void onAddPhotoClicked(MutableSubCriteria subCriteria) {
        presenter.onAddPhotoClicked(subCriteria);
    }

    @Override
    public void onRemovePhotoClicked(MutableSubCriteria subCriteria, String photoPath) {
        presenter.onDeletePhotoClicked(subCriteria, photoPath);
    }

    @Override
    public void onMorePhotosClick(MutableSubCriteria subCriteria) {
        presenter.onMorePhotosClick(subCriteria);
    }

    @Override
    public void navigateToPhotos(MutableSubCriteria subCriteria) {
        startActivityForResult(PhotosActivity.createIntent(this, subCriteria), REQUEST_CHANGES);
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
        String transitionName = ViewCompat.getTransitionName(anchor);

        Intent intent = FullscreenImageActivity.createIntent(this, photoPath, transitionName);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, anchor, transitionName);
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void notifySubCriteriaChanged(MutableSubCriteria subCriteria) {
        recyclerAdapter.notify(subCriteria);
    }

    @Override
    public void showCommentEditor(MutableSubCriteria subCriteria) {
        CommentDialogFragment dialog = CommentDialogFragment.create(subCriteria);
        dialog.show(getSupportFragmentManager(), TAG_DIALOG);
    }

    @Override
    public void onCommentSubmit(String comment) {
        presenter.onCommentEdit(comment);
    }

    private void applyIcon(ImageView imageView, @Nullable Integer resourceIndex, boolean isHighlighted) {
        if (resourceIndex != null) {
            imageView.setImageResource(resourceIndex);
            imageView.setActivated(isHighlighted);
        }
    }
}
