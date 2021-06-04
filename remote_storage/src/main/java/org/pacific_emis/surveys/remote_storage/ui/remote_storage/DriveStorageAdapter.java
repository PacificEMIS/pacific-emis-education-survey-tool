package org.pacific_emis.surveys.remote_storage.ui.remote_storage;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import org.pacific_emis.surveys.remote_storage.R;
import org.pacific_emis.surveys.remote_storage.data.model.GoogleDriveFileHolder;

public class DriveStorageAdapter extends BaseListAdapter<GoogleDriveFileHolder> {


    public DriveStorageAdapter(@Nullable OnItemClickListener<GoogleDriveFileHolder> clickListener,
                               @Nullable OnItemLongClickListener<GoogleDriveFileHolder> longClickListener) {
        super(clickListener, longClickListener);
    }

    public DriveStorageAdapter(@Nullable OnItemClickListener<GoogleDriveFileHolder> clickListener) {
        super(clickListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private final Drawable folderDrawable = getResources().getDrawable(R.drawable.ic_folder, getContext().getTheme());
        private final Drawable fileDrawable = getResources().getDrawable(R.drawable.ic_file, getContext().getTheme());
        private final TextView nameTextView = findViewById(R.id.textview_name);
        private final ImageView iconImageView = findViewById(R.id.imageview_icon);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_file_holder);
        }

        @Override
        protected void onBind(GoogleDriveFileHolder item) {
            nameTextView.setText(item.getName());
            switch (item.getMimeType()) {
                case FOLDER:
                    iconImageView.setImageDrawable(folderDrawable);
                    break;
                case XML:
                case FILE:
                    iconImageView.setImageDrawable(fileDrawable);
                    break;
                default:
                    iconImageView.setImageDrawable(null);
                    break;
            }
        }
    }
}
