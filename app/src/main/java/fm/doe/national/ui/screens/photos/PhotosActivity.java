package fm.doe.national.ui.screens.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.mutable.MutablePhoto;
import fm.doe.national.ui.custom_views.photos_view.PhotosAdapter;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.criterias.FullscreenImageActivity;

public class PhotosActivity extends BaseActivity implements PhotosView, PhotosAdapter.Callback {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private static final String EXTRA_STANDARD_ID = "EXTRA_STANDARD_ID";
    private static final String EXTRA_CRITERIA_ID = "EXTRA_CRITERIA_ID";
    private static final String EXTRA_SUBCRITERIA_ID = "EXTRA_SUBCRITERIA_ID";


    private final PhotosAdapter adapter = new PhotosAdapter();

    @BindView(R.id.recyclerview_photos)
    RecyclerView photosRecycler;

    @InjectPresenter
    PhotosPresenter presenter;

    public static Intent createIntent(Context context, long categoryId, long standardId, long criteriaId, long subCriteriaId) {
        return new Intent(context, PhotosActivity.class)
                .putExtra(EXTRA_CATEGORY_ID, categoryId)
                .putExtra(EXTRA_STANDARD_ID, standardId)
                .putExtra(EXTRA_CRITERIA_ID, criteriaId)
                .putExtra(EXTRA_SUBCRITERIA_ID, subCriteriaId);
    }

    @ProvidePresenter
    PhotosPresenter providePresenter() {
        Intent intent = getIntent();
        return new PhotosPresenter(
                intent.getLongExtra(EXTRA_CATEGORY_ID, -1),
                intent.getLongExtra(EXTRA_STANDARD_ID, -1),
                intent.getLongExtra(EXTRA_CRITERIA_ID, -1),
                intent.getLongExtra(EXTRA_SUBCRITERIA_ID, -1)
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
        photosRecycler.setAdapter(adapter);
        adapter.setCallback(this);
    }

    @Override
    public void showPhotos(List<MutablePhoto> photos) {
        adapter.setItems(photos);
    }

    @Override
    public void onDeletePhotoClick(MutablePhoto photo) {
        presenter.onDeletePhotoClick(photo);
    }

    @Override
    public void onPhotoClick(View v, MutablePhoto photo) {
        String transitionName = ViewCompat.getTransitionName(v);

        Intent intent = FullscreenImageActivity.createIntent(this, photo.getLocalPath(), transitionName);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, transitionName);
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
