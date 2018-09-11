package fm.doe.national.ui.screens.criterias;

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

public class PhotosAdapter extends BaseAdapter<String> {

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

        PhotoViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo_proof);
        }

        @Override
        protected void onBind(String item) {
            File imgFile = new File(item);
            if (imgFile.exists()) ViewUtils.setScaledDownImageTo(photoImageView, imgFile.getAbsolutePath());
        }

        @OnClick({R.id.imagebutton_delete, R.id.imageview_photo})
        public void onViewClick(View v) {
            String item = getItem();
            switch (v.getId()) {
                case R.id.imagebutton_delete:
                    if (callback != null) callback.onRemovePhotoClicked(parentSubCriteria, item);
                    break;
                case R.id.imageview_photo:
                    if (callback != null) callback.onPhotoClicked(v, item);
                    break;
            }
        }
    }
}
