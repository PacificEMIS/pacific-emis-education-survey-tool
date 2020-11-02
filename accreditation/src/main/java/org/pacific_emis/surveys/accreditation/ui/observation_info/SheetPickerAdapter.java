package org.pacific_emis.surveys.accreditation.ui.observation_info;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.List;

import org.pacific_emis.surveys.accreditation.R;

public class SheetPickerAdapter extends BaseListAdapter<String> {

    public SheetPickerAdapter(@NonNull List<String> items, @Nullable OnItemClickListener<String> clickListener) {
        super(items, clickListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SheetItemViewHolder(parent);
    }

    private class SheetItemViewHolder extends ViewHolder {

        public SheetItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sheet_picker);
        }

        @Override
        protected void onBind(String item) {
            ((TextView) itemView).setText(item);
        }

    }
}
