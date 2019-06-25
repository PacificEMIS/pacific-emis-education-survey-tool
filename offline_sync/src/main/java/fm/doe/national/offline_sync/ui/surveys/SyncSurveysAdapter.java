package fm.doe.national.offline_sync.ui.surveys;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.offline_sync.R;

public class SyncSurveysAdapter extends BaseListAdapter<Survey> {

    public SyncSurveysAdapter(@Nullable OnItemClickListener<Survey> clickListener) {
        super(clickListener);
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private final TextView schoolIdTextView = findViewById(R.id.textview_id_school);
        private final TextView nameSchoolTextView = findViewById(R.id.textview_name_school);
        private final TextView creationDateTextView = findViewById(R.id.textview_creation_date);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sync_survey);
        }

        @Override
        public void onBind(Survey item) {
            schoolIdTextView.setText(item.getSchoolId());
            nameSchoolTextView.setText(item.getSchoolName());
            creationDateTextView.setText(DateUtils.formatUiText(item.getDate()));
        }

    }
}
