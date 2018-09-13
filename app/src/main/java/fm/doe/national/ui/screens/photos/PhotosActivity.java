package fm.doe.national.ui.screens.photos;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.custom_views.photos_view.PhotosAdapter;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.criterias.FullscreenImageActivity;

public class PhotosActivity extends BaseActivity implements PhotosView, PhotosAdapter.Callback {

    public static final int REQUEST_CHANGES = 432;
    private static final String EXTRA_PASSING = "EXTRA_PASSING";
    private static final String EXTRA_SUBCRITERIA = "EXTRA_SUBCRITERIA";


    private final PhotosAdapter adapter = new PhotosAdapter();

    @BindView(R.id.recyclerview_photos)
    RecyclerView photosRecycler;

    @InjectPresenter
    PhotosPresenter presenter;

    public static Intent createIntent(Context context, long passingId, SubCriteria subCriteria) {
        List<String> photos = subCriteria.getAnswer().getPhotos();
        String[] photosAsArray = photos.toArray(new String[photos.size()]);
        return new Intent(context, PhotosActivity.class)
                .putExtra(EXTRA_PASSING, passingId)
                .putExtra(EXTRA_SUBCRITERIA, subCriteria.getId());
    }

    @ProvidePresenter
    PhotosPresenter providePresenter() {
        Intent intent = getIntent();
        return new PhotosPresenter(intent.getLongExtra(EXTRA_PASSING, -1),
                intent.getLongExtra(EXTRA_SUBCRITERIA, -1));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_photos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_attached_photos);
        photosRecycler.setAdapter(adapter);
        adapter.setCallback(this);
    }

    @Override
    public void showPhotos(List<String> photos) {
        adapter.setItems(photos);
    }

    @Override
    public void onDeletePhotoClick(String photo) {
        presenter.onDeletePhotoClick(photo);
    }

    @Override
    public void onPhotoClick(View v, String photo) {
        Intent intent = FullscreenImageActivity.createIntent(this, photo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(v, FullscreenImageActivity.TRANSITION_IMAGE);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, v, FullscreenImageActivity.TRANSITION_IMAGE);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
