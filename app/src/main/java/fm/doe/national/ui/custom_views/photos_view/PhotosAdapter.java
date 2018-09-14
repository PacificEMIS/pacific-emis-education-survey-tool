package fm.doe.national.ui.custom_views.photos_view;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class PhotosAdapter extends BaseAdapter<String> {

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
        protected void onBind(String item) {
            File imgFile = new File(item);
            if (imgFile.exists()) ViewUtils.setScaledDownImageTo(photoImageView, imgFile.getAbsolutePath());

            ViewCompat.setTransitionName(photoImageView, item); // using photo path as unique transition name
        }

        @OnClick({R.id.imagebutton_delete, R.id.imageview_photo})
        public void onViewClick(View v) {
            String item = getItem();
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
        void onDeletePhotoClick(String photo);
        void onPhotoClick(View v, String photo);
    }
}
