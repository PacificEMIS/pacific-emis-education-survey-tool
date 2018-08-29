package fm.doe.national.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.listeners.FilePickerListener;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;
import fm.doe.national.ui.screens.cloud.DropboxView;

public class FilePickerAdapter extends OmegaRecyclerView.Adapter<FilePickerAdapter.BrowsingItemViewHolder> {

    private List<BrowsingTreeObject> items = new ArrayList<>();
    private DropboxView.PickerType kind = DropboxView.PickerType.FILE;

    @Nullable
    private FilePickerListener listener;

    public void setItems(@NonNull List<BrowsingTreeObject> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setListener(@Nullable FilePickerListener listener) {
        this.listener = listener;
    }

    public void setPickerType(DropboxView.PickerType pickerType) {
        this.kind = pickerType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BrowsingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BrowsingItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowsingItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BrowsingItemViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.imageview_browsing_icon)
        ImageView iconImageView;

        @BindView(R.id.textview_browsing_name)
        TextView nameTextView;

        private BrowsingTreeObject object;

        BrowsingItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_browsing_content);
        }

        void bind(BrowsingTreeObject object) {
            this.object = object;

            boolean isObjectEnabled = object.isDirectory() || kind == DropboxView.PickerType.FILE;
            if (!isObjectEnabled) {
                itemView.setOnClickListener(null);
                nameTextView.setTextColor(getResources().getColor(R.color.soft_grey));
            } else {
                itemView.setOnClickListener(this);
                nameTextView.setTextColor(getResources().getColor(R.color.black));
            }
            nameTextView.setText(object.getName());
            iconImageView.setImageResource(object.isDirectory() ? R.drawable.ic_folder_24dp : R.drawable.ic_file_24dp);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onBrowsingObjectPicked(object);
            }
        }
    }


}
