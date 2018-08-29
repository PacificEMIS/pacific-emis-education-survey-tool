package fm.doe.national.ui.screens.cloud;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class FilePickerAdapter extends BaseClickableAdapter<BrowsingTreeObject, FilePickerAdapter.BrowsingItemViewHolder> {

    private DropboxView.PickerType kind = DropboxView.PickerType.FILE;

    public void setPickerType(DropboxView.PickerType pickerType) {
        this.kind = pickerType;
        notifyDataSetChanged();
    }

    @Override
    protected BrowsingItemViewHolder provideViewHolder(ViewGroup parent) {
        return new BrowsingItemViewHolder(parent);
    }

    class BrowsingItemViewHolder extends BaseRecyclerViewHolder<BrowsingTreeObject> implements View.OnClickListener {

        @BindView(R.id.imageview_browsing_icon)
        ImageView iconImageView;

        @BindView(R.id.textview_browsing_name)
        TextView nameTextView;

        BrowsingItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_browsing_content);
        }

        @Override
        public void onBind() {
            boolean isObjectEnabled = item.isDirectory() || kind == DropboxView.PickerType.FILE;
            if (!isObjectEnabled) {
                itemView.setOnClickListener(null);
                nameTextView.setTextColor(getResources().getColor(R.color.soft_grey));
            } else {
                itemView.setOnClickListener(this);
                nameTextView.setTextColor(getResources().getColor(R.color.black));
            }
            nameTextView.setText(item.getName());
            iconImageView.setImageResource(item.isDirectory() ? R.drawable.ic_folder_24dp : R.drawable.ic_file_24dp);
        }

        @Override
        public void onClick(View view) {
            onItemClick(item);
        }
    }


}
