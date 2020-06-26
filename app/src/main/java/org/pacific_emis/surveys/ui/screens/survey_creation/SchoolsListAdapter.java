package org.pacific_emis.surveys.ui.screens.survey_creation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;

public class SchoolsListAdapter extends BaseAdapter<School> {

    private int selectedPosition = RecyclerView.NO_POSITION;

    SchoolsListAdapter(OnItemClickListener<School> clickListener) {
        super(clickListener);
    }

    @Override
    protected SchoolViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolViewHolder(parent);
    }

    private void updateSelection(int newSelectedPosition) {
        int oldSelection = selectedPosition;
        selectedPosition = newSelectedPosition;
        notifyItemChanged(oldSelection);
        notifyItemChanged(selectedPosition);
    }

    class SchoolViewHolder extends ViewHolder {

        @BindView(R.id.textview_name)
        TextView nameTextView;

        @BindView(R.id.textview_id)
        TextView idTextView;

        @BindView(R.id.radiobutton)
        RadioButton radioButton;

        SchoolViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_school);
        }

        @Override
        public void onBind(School item) {
            idTextView.setText(item.getId());
            nameTextView.setText(item.getName());
            boolean isSelected = getAdapterPosition() == selectedPosition;
            itemView.setActivated(isSelected);
            radioButton.setChecked(isSelected);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            updateSelection(getAdapterPosition());
        }
    }
}
