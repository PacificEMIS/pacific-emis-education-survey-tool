package fm.doe.national.accreditation.ui.photos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.io.File;
import java.util.List;

import fm.doe.national.accreditation.R;
import fm.doe.national.cloud.di.CloudComponentInjector;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.di.CoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.Constants;

public class PhotosActivity extends BaseActivity implements
        PhotosView,
        PhotosAdapter.Callback,
        View.OnClickListener {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private static final String EXTRA_STANDARD_ID = "EXTRA_STANDARD_ID";
    private static final String EXTRA_CRITERIA_ID = "EXTRA_CRITERIA_ID";
    private static final String EXTRA_SUB_CRITERIA_ID = "EXTRA_SUB_CRITERIA_ID";
    private static final int REQUEST_CAMERA = 100;

    private final PhotosAdapter adapter = new PhotosAdapter();

    @InjectPresenter
    PhotosPresenter presenter;

    private RecyclerView photosRecycler;
    private FloatingActionButton floatingActionButton;

    public static Intent createIntent(Context context, long categoryId, long standardId, long criteriaId, long subCriteriaId) {
        return new Intent(context, PhotosActivity.class)
                .putExtra(EXTRA_CATEGORY_ID, categoryId)
                .putExtra(EXTRA_STANDARD_ID, standardId)
                .putExtra(EXTRA_CRITERIA_ID, criteriaId)
                .putExtra(EXTRA_SUB_CRITERIA_ID, subCriteriaId);
    }

    @ProvidePresenter
    PhotosPresenter providePresenter() {
        Intent intent = getIntent();
        Application application = getApplication();
        return new PhotosPresenter(
                CoreComponentInjector.getComponent(application),
                CloudComponentInjector.getComponent(application),
                intent.getLongExtra(EXTRA_CATEGORY_ID, -1),
                intent.getLongExtra(EXTRA_STANDARD_ID, -1),
                intent.getLongExtra(EXTRA_CRITERIA_ID, -1),
                intent.getLongExtra(EXTRA_SUB_CRITERIA_ID, -1)
        );
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_photos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_attached_photos);
        photosRecycler = findViewById(R.id.recyclerview_photos);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        photosRecycler.setAdapter(adapter);
        adapter.setCallback(this);
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

    @Override
    public void showPhotos(List<Photo> photos) {
        adapter.setItems(photos);
    }

    @Override
    public void onDeletePhotoClick(Photo photo) {
        presenter.onDeletePhotoClick(photo);
    }

    @Override
    public void onPhotoClick(View v, Photo photo) {
        String transitionName = ViewCompat.getTransitionName(v);

        Intent intent = FullscreenImageActivity.createIntent(this, photo.getLocalPath(), transitionName);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, transitionName);
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            presenter.onAddPhotoPressed();
        }
    }

    @Override
    public void takePictureTo(@NonNull File file) {
        PackageManager pm = getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            showToast(Text.from(R.string.error_take_picture));
            presenter.onTakePhotoFailure();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(pm) != null) {
            Uri photoURI = FileProvider.getUriForFile(this, Constants.AUTHORITY_FILE_PROVIDER, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
