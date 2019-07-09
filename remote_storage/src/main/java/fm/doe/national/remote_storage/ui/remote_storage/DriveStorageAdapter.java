package fm.doe.national.remote_storage.ui.remote_storage;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;

public class DriveStorageAdapter extends BaseListAdapter<GoogleDriveFileHolder> {

    public DriveStorageAdapter(@Nullable OnItemClickListener<GoogleDriveFileHolder> clickListener) {
        super(clickListener);
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private final TextView nameTextView = findViewById(R.id.textview_name);
        private final ImageView iconImageView = findViewById(R.id.imageview_icon);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_file_holder);
        }

        @Override
        protected void onBind(GoogleDriveFileHolder item) {
            if (item.getId() == null) {
                nameTextView.setText(null);
                iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp, getContext().getTheme()));
            } else {
                nameTextView.setText(item.getName());
                switch (item.getMimeType()) {
                    case FOLDER:
                        iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_folder, getContext().getTheme()));
                        break;
                    case PLAIN_TEXT:
                    case FILE:
                        iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_file, getContext().getTheme()));
                        break;
                }
            }
        }
    }
}
