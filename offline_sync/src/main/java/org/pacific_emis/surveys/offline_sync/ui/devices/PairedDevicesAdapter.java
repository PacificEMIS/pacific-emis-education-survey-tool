package org.pacific_emis.surveys.offline_sync.ui.devices;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.ui.screens.base.BaseSelectionListAdapter;
import org.pacific_emis.surveys.offline_sync.R;
import org.pacific_emis.surveys.offline_sync.data.model.Device;

public class PairedDevicesAdapter extends BaseSelectionListAdapter<Device> {

    public PairedDevicesAdapter(@Nullable OnItemClickListener<Device> clickListener) {
        super(clickListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends SelectionViewHolder {

        private final TextView nameTextView = findViewById(R.id.textview_name);
        private final TextView macTextView = findViewById(R.id.textview_mac);

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_bluetooth_device);
            radioButton = findViewById(R.id.radiobutton);
        }

        @Override
        protected void onBind(Device item) {
            super.onBind(item);
            nameTextView.setText(item.getName() == null ? getString(R.string.title_no_name) : item.getName());
            macTextView.setText(item.getAddress());
        }
    }
}
