package fm.doe.national.survey_core.ui.survey;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.survey_core.R;

public class PhotosPreviewAdapter extends BaseListAdapter<Photo> {
    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo_preview);
        }

        @Override
        protected void onBind(Photo item) {
            ImageView imageView = (ImageView) itemView;
            ViewUtils.setImageTo(imageView, item.getImage());
        }
    }
}
