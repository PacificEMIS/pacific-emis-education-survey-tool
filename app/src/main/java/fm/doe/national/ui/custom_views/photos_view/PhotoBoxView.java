package fm.doe.national.ui.custom_views.photos_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.data.model.Photo;

public class PhotoBoxView extends FrameLayout implements PhotosAdapter.Callback {

    private final int maxPhotos;
    private final int countPhotosExtended;

    private final PhotosAdapter adapter = new PhotosAdapter();

    @Nullable
    private Callback callback;

    @BindView(R.id.recyclerview_photos)
    OmegaRecyclerView photosRecyclerView;

    @BindView(R.id.layout_more_photos)
    View morePhotosView;

    @BindView(R.id.textview_more_photos)
    TextView morePhotosTextView;

    public PhotoBoxView(Context context) {
        this(context, null);
    }

    public PhotoBoxView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoBoxView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        inflate(context, R.layout.view_photos, this);
        ButterKnife.bind(this);
        photosRecyclerView.setAdapter(adapter);
        adapter.setCallback(this);
        maxPhotos = getContext().getResources().getInteger(R.integer.count_spans_photos_preview);
        countPhotosExtended = maxPhotos - 2; // 2 == spans { addPhoto, morePhotos }
    }

    public void setPhotos(List<Photo> photos) {
        if (photos.size() < maxPhotos) {
            adapter.setItems(photos);
            photosRecyclerView.setFootersVisibility(false);
        } else {
            adapter.setItems(photos.subList(0, countPhotosExtended));
            morePhotosTextView.setText(getContext().getString(
                    R.string.more_photos_pattern,
                    photos.size() - countPhotosExtended));
            photosRecyclerView.setFootersVisibility(true);
        }
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    @OnClick(R.id.imagebutton_add_photo)
    public void onAddPhotoClick(View v) {
        if (callback != null) callback.onAddPhotoClick();
    }

    @OnClick(R.id.textview_more_photos)
    public void onMorePhotosClick(View v) {
        if (callback != null) callback.onMorePhotosClick();
    }

    @Override
    public void onDeletePhotoClick(Photo photo) {
        if (callback != null) callback.onDeletePhotoClick(photo);
    }

    @Override
    public void onPhotoClick(View v, Photo photo) {
        if (callback != null) callback.onPhotoClick(v, photo);
    }

    public interface Callback {
        void onAddPhotoClick();
        void onMorePhotosClick();
        void onDeletePhotoClick(Photo photo);
        void onPhotoClick(View v, Photo photo);
    }
}
