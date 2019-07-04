package fm.doe.national.offline_sync.ui.surveys;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.offline_sync.R;

public class SyncSurveysAdapter extends BaseListAdapter<Survey> {

    private int selectedPosition = RecyclerView.NO_POSITION;

    public SyncSurveysAdapter(@Nullable OnItemClickListener<Survey> clickListener) {
        super(clickListener);
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private final TextView creationDateTextView = findViewById(R.id.textview_date);
        private final TextView nameSchoolTextView = findViewById(R.id.textview_name);
        private final RadioButton radioButton = findViewById(R.id.radiobutton);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sync_survey);
        }

        @Override
        public void onBind(Survey item) {
            nameSchoolTextView.setText(item.getSchoolName());
            creationDateTextView.setText(DateUtils.formatUiText(item.getDate()));

            itemView.setActivated(isSelected());
            radioButton.setChecked(isSelected());
        }

        private boolean isSelected() {
            return selectedPosition == getAdapterPosition();
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

            if (selectedPosition != getAdapterPosition()) {
                int oldSelectedPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(oldSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        }
    }
}
