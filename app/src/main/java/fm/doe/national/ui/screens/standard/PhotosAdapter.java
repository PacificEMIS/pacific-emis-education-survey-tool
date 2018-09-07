package fm.doe.national.ui.screens.standard;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class PhotosAdapter extends BaseAdapter<PhotoViewData> {

    private SubCriteria parentSubCriteria;

    @Nullable
    private SubcriteriaCallback callback = null;

    public void setCallback(@Nullable SubcriteriaCallback callback) {
        this.callback = callback;
    }

    public void setParentSubCriteria(SubCriteria parentSubCriteria) {
        this.parentSubCriteria = parentSubCriteria;
    }

    @Override
    protected PhotoViewHolder provideViewHolder(ViewGroup parent) {
        return new PhotoViewHolder(parent);
    }

    class PhotoViewHolder extends ViewHolder {

        @BindView(R.id.imageview_photo)
        ImageView photoImageView;

        @BindView(R.id.imageview_add_button)
        ImageView addImageView;

        @BindView(R.id.imageview_delete_button)
        View deleteImageView;

        PhotoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo_proof);
        }

        @Override
        protected void onBind(PhotoViewData item) {
            switch (item.getType()) {
                case ADDER:
                    photoImageView.setVisibility(View.GONE);
                    addImageView.setVisibility(View.VISIBLE);
                    deleteImageView.setVisibility(View.GONE);
                    break;
                case PHOTO:
                    photoImageView.setVisibility(View.VISIBLE);
                    addImageView.setVisibility(View.GONE);
                    deleteImageView.setVisibility(View.VISIBLE);
                    File imgFile = new File(item.getPath());
                    if (imgFile.exists()) ViewUtils.setScaledDownImageTo(photoImageView, imgFile.getAbsolutePath());
                    break;
            }
        }

        @OnClick({R.id.imageview_delete_button, R.id.imageview_photo, R.id.imageview_add_button})
        public void onViewClick(View v) {
            PhotoViewData item = getItem();
            switch (v.getId()) {
                case R.id.imageview_delete_button:
                    if (callback != null) callback.onRemovePhotoClicked(parentSubCriteria, item.getPath());
                    break;
                case R.id.imageview_photo:
                    if (callback != null) callback.onPhotoClicked(v, item.getPath());
                    break;
                case R.id.imageview_add_button:
                    if (callback != null) callback.onAddPhotoClicked(parentSubCriteria);
                    break;
            }
        }
    }
}
