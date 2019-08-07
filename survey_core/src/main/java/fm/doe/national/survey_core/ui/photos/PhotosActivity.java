package fm.doe.national.survey_core.ui.photos;

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

import java.io.File;
import java.util.List;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.Constants;
import fm.doe.national.survey_core.R;

public abstract class PhotosActivity extends BaseActivity implements
        PhotosView,
        PhotosAdapter.Callback,
        View.OnClickListener {

    private static final int REQUEST_CAMERA = 100;

    private final PhotosAdapter adapter = new PhotosAdapter();

    private RecyclerView photosRecycler;
    private FloatingActionButton floatingActionButton;

    protected abstract PhotosPresenter getPresenter();

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
    protected int getContentView() {
        return R.layout.activity_photos;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    getPresenter().onTakePhotoSuccess();
                } else {
                    getPresenter().onTakePhotoFailure();
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
        getPresenter().onDeletePhotoClick(photo);
    }

    @Override
    public void onPhotoClick(View v, Photo photo) {
        String transitionName = ViewCompat.getTransitionName(v);

        Intent intent = FullscreenImageActivity.createIntent(this, photo.getRemotePath(), transitionName);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, transitionName);
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            getPresenter().onAddPhotoPressed();
        }
    }

    @Override
    public void takePictureTo(@NonNull File file) {
        PackageManager pm = getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            showToast(Text.from(R.string.error_take_picture));
            getPresenter().onTakePhotoFailure();
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
