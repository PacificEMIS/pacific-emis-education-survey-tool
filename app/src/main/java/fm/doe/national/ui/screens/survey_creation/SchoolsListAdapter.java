package fm.doe.national.ui.screens.survey_creation;

import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class SchoolsListAdapter extends BaseAdapter<School> {

    @Override
    protected SchoolViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolViewHolder(parent);
    }

    class SchoolViewHolder extends ViewHolder {

        @BindView(R.id.textview_name)
        TextView nameTextView;

        SchoolViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_school);
        }

        @Override
        public void onBind(School item) {
            nameTextView.setText(item.getName());
        }
    }
}
