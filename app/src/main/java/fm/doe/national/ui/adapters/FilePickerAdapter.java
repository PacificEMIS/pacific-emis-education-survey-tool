package fm.doe.national.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.listeners.FilePickerListener;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class FilePickerAdapter extends OmegaRecyclerView.Adapter<FilePickerAdapter.BrowsingItemViewHolder> {

    private BrowsingTreeObject item;
    private Kind kind = Kind.FILE;

    @Nullable
    private FilePickerListener listener;

    public void setItem(@NonNull BrowsingTreeObject item) {
        this.item = item;
        notifyDataSetChanged();
    }

    public BrowsingTreeObject getItem() {
        return item;
    }

    public void setListener(@Nullable FilePickerListener listener) {
        this.listener = listener;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BrowsingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BrowsingItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowsingItemViewHolder holder, int position) {
        holder.bind(item.getChilds().get(position));
    }

    @Override
    public int getItemCount() {
        return item.getChilds().size();
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

            boolean isObjectEnabled = object.isDirectory() || kind == Kind.FILE;
            if (!isObjectEnabled) {
                itemView.setClickable(false);
                nameTextView.setTextColor(getResources().getColor(R.color.soft_grey));
            }


            itemView.setOnClickListener(this);

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

    public enum Kind {
        FILE, FOLDER
    }
}
