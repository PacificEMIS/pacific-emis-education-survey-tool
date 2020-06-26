package org.pacific_emis.surveys.survey_core.ui.photos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.survey_core.R;

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
            ViewUtils.setImageTo(photoImageView, item.getImage());
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
