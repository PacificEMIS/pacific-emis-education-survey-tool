package fm.doe.national.cloud.ui.cloud.dropbox;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fm.doe.national.cloud.R;
import fm.doe.national.cloud.model.dropbox.BrowsingTreeObject;
import fm.doe.national.core.ui.screens.base.BaseAdapter;

public class FilePickerListAdapter extends BaseAdapter<BrowsingTreeObject> {

    private DropboxView.PickerType kind = DropboxView.PickerType.FILE;

    public FilePickerListAdapter(OnItemClickListener<BrowsingTreeObject> clickListener) {
        super(clickListener);
    }

    public void setPickerType(DropboxView.PickerType pickerType) {
        this.kind = pickerType;
        notifyDataSetChanged();
    }

    @Override
    protected BrowsingItemViewHolder provideViewHolder(ViewGroup parent) {
        return new BrowsingItemViewHolder(parent);
    }

    class BrowsingItemViewHolder extends ViewHolder {

        private final ImageView iconImageView = findViewById(R.id.imageview_browsing_icon);
        private final TextView nameTextView = findViewById(R.id.textview_browsing_name);

        BrowsingItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_browsing_content);
        }

        @Override
        public void onBind(BrowsingTreeObject item) {
            boolean isObjectEnabled = item.isDirectory() || kind == DropboxView.PickerType.FILE;
            if (!isObjectEnabled) {
                itemView.setOnClickListener(null);
                nameTextView.setAlpha(0.5f);
            } else {
                itemView.setOnClickListener(this);
                nameTextView.setAlpha(1.0f);
            }
            nameTextView.setText(item.getName());
            iconImageView.setImageResource(item.isDirectory() ? R.drawable.ic_folder_24dp : R.drawable.ic_file_24dp);
        }
    }
}
