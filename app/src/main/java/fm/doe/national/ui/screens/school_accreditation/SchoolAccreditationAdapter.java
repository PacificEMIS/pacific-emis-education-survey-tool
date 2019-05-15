package fm.doe.national.ui.screens.school_accreditation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.DateUtils;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.data.model.Survey;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class SchoolAccreditationAdapter extends BaseAdapter<Survey> {

    public SchoolAccreditationAdapter(
            OnItemClickListener<Survey> clickListener,
            OnItemLongClickListener<Survey> longClickListener) {
        super(clickListener, longClickListener);
    }

    @Override
    protected SchoolAccreditationViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolAccreditationViewHolder(parent);
    }

    protected class SchoolAccreditationViewHolder extends ViewHolder implements View.OnLongClickListener {

        @BindView(R.id.textview_id_school)
        TextView schoolIdTextView;

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
        public void onBind(Survey item) {
            schoolIdTextView.setText(item.getSchoolId());
            nameSchoolTextView.setText(item.getSchoolName());
            createdYearTextView.setText(DateUtils.formatMonthYear(item.getDate()));

            ViewUtils.rebindProgress(
                    item.getProgress().total,
                    item.getProgress().completed,
                    getString(R.string.criteria_progress),
                    progressTextView, progressBar);
        }
    }
}