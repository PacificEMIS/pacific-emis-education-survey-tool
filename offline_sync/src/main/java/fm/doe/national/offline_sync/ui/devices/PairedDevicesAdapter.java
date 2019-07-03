package fm.doe.national.offline_sync.ui.devices;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.offline_sync.R;
import fm.doe.national.offline_sync.data.model.Device;

public class PairedDevicesAdapter extends BaseListAdapter<Device> {

    private int selectedPosition = RecyclerView.NO_POSITION;

    public PairedDevicesAdapter(@Nullable OnItemClickListener<Device> clickListener) {
        super(clickListener);
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private final TextView nameTextView = findViewById(R.id.textview_name);
        private final TextView macTextView = findViewById(R.id.textview_mac);

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_bluetooth_device);
        }

        @Override
        protected void onBind(Device item) {
            nameTextView.setText(item.getName() == null ? getString(R.string.title_no_name) : item.getName());
            macTextView.setText(item.getAddress());

            itemView.setActivated(isSelected());
        }

        private boolean isSelected() {
            return selectedPosition == getAdapterPosition();
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

            if (isSelected()) {
                selectedPosition = RecyclerView.NO_POSITION;
                notifyItemChanged(getAdapterPosition());
            } else {
                int oldSelectedPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(oldSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        }
    }
}
