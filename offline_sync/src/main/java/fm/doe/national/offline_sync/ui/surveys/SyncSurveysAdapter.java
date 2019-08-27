package fm.doe.national.offline_sync.ui.surveys;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseSelectionListAdapter;
import fm.doe.national.offline_sync.R;

public class SyncSurveysAdapter extends BaseSelectionListAdapter<Survey> {

    public SyncSurveysAdapter(@Nullable OnItemClickListener<Survey> clickListener) {
        super(clickListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends SelectionViewHolder {

        private final TextView creationDateTextView = findViewById(R.id.textview_date);
        private final TextView nameSchoolTextView = findViewById(R.id.textview_name);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sync_survey);
            radioButton = findViewById(R.id.radiobutton);
        }

        @Override
        public void onBind(Survey item) {
            super.onBind(item);
            nameSchoolTextView.setText(item.getSchoolName());
            creationDateTextView.setText(item.getSurveyTag());
        }

    }

}
