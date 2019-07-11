package fm.doe.national.survey_core.ui.photos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;

import java.io.File;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.survey_core.R;

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

        ImageView photoImageView;

        PhotoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo);
            photoImageView = findViewById(R.id.imageview_photo);
            findViewById(R.id.imagebutton_delete).setOnClickListener(this);
            findViewById(R.id.imageview_photo).setOnClickListener(this);
        }

        @Override
        protected void onBind(Photo item) {
            File imgFile = new File(item.getLocalPath());

            if (imgFile.exists()) {
                Glide.with(getContext())
                        .load(imgFile)
                        .into(photoImageView);
            } else if (item.getRemotePath() != null) {
                Glide.with(getContext())
                        .load(item.getRemotePath())
                        .into(photoImageView);
            }

            ViewCompat.setTransitionName(photoImageView, item.getLocalPath()); // using photo path as unique transition name
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imagebutton_delete) {
                if (callback != null) callback.onDeletePhotoClick(getItem());
            } else if (v.getId() == R.id.imageview_photo) {
                if (callback != null) callback.onPhotoClick(v, getItem());
            } else {
                super.onClick(v);
            }
        }
    }

    public interface Callback {
        void onDeletePhotoClick(Photo photo);
        void onPhotoClick(View v, Photo photo);
    }
}
