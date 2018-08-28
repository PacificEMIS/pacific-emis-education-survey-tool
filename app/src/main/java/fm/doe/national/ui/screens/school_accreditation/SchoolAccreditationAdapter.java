package fm.doe.national.ui.screens.school_accreditation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.ModelsExt;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class SchoolAccreditationAdapter extends
        BaseClickableAdapter<SchoolAccreditationPassing, SchoolAccreditationAdapter.SchoolAccreditationViewHolder> {

    @Override
    protected SchoolAccreditationViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolAccreditationViewHolder(parent);
    }

    protected class SchoolAccreditationViewHolder extends
            BaseRecyclerViewHolder<SchoolAccreditationPassing> implements View.OnClickListener {

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
        public void onBind() {
            nameSchoolTextView.setText(item.getSchool().getName());
            createdYearTextView.setText(String.valueOf(item.getYear()));

            itemView.setOnClickListener(this);

            rebindProgress(
                    ModelsExt.getTotalQuestionsCount(item.getSchoolAccreditation()),
                    ModelsExt.getAnsweredQuestionsCount(item.getSchoolAccreditation()),
                    progressTextView, progressBar);
        }

        @Override
        public void onClick(View v) {
            onItemClick(item);
        }

    }
}