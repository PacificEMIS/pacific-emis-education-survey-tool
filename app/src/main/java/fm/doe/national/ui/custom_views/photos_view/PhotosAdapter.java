package fm.doe.national.ui.custom_views.photos_view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class PhotosAdapter extends BaseAdapter<Photo> {

    @Nullable
    private Callback callback = null;

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    @Override
    protected PhotoViewHolder provideViewHolder(ViewGroup parent) {
        return new PhotoViewHolder(parent);
    }

    class PhotoViewHolder extends ViewHolder {

        @BindView(R.id.imageview_photo)
        ImageView photoImageView;

        PhotoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo);
        }

        @Override
        protected void onBind(Photo item) {
            File imgFile = new File(item.getLocalPath());
            if (imgFile.exists()) ViewUtils.setScaledDownImageTo(photoImageView, imgFile.getAbsolutePath());

            ViewCompat.setTransitionName(photoImageView, item.getLocalPath()); // using photo path as unique transition name
        }

        @OnClick({R.id.imagebutton_delete, R.id.imageview_photo})
        public void onViewClick(View v) {
            Photo item = getItem();
            switch (v.getId()) {
                case R.id.imagebutton_delete:
                    if (callback != null) callback.onDeletePhotoClick(item);
                    break;
                case R.id.imageview_photo:
                    if (callback != null) callback.onPhotoClick(v, item);
                    break;
            }
        }
    }

    public interface Callback {
        void onDeletePhotoClick(Photo photo);
        void onPhotoClick(View v, Photo photo);
    }
}
