package fm.doe.national.ui.screens.school_accreditation;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class SchoolAccreditationAdapter extends BaseAdapter<SchoolAccreditationPassing> {

    @Override
    protected SchoolAccreditationViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolAccreditationViewHolder(parent);
    }

    protected class SchoolAccreditationViewHolder extends ViewHolder {

        @BindView(R.id.textview_name_school)
        TextView nameSchoolTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.textview_created_year)
        TextView createdYearTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        SchoolAccreditationViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_survey);
        }

        @Override
        public void onBind(SchoolAccreditationPassing item) {
            nameSchoolTextView.setText(item.getSchool().getName());
            createdYearTextView.setText(String.valueOf(item.getYear()));

            itemView.setOnClickListener(this);

            ViewUtils.rebindProgress(
                    item.getSchoolAccreditation().getProgress().getTotalItemsCount(),
                    item.getSchoolAccreditation().getProgress().getCompletedItemsCount(),
                    getString(R.string.criteria_progress),
                    progressTextView, progressBar);
        }

    }
}