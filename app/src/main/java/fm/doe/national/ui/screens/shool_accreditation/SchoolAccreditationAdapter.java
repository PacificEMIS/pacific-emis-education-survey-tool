package fm.doe.national.ui.screens.shool_accreditation;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.models.survey.School;
import fm.doe.national.ui.screens.base.BaseRecyclerAdapter;

/**
 * Created by Alexander Chibirev on 8/16/2018.
 */
public class SchoolAccreditationAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseViewHolder> {

    private List<MockSchool> schools = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(inflateView(parent, R.layout.item_school));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ((SchoolViewHolder) holder).update(schools.get(position));
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public void updateSchools(List<MockSchool> schools) {
        this.schools = schools;
        notifyDataSetChanged();
    }

    class SchoolViewHolder extends BaseViewHolder {

        @BindView(R.id.textview_name_school)
        TextView nameSchoolTextview;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.textview_created_year)
        TextView createdYearTextview;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        public SchoolViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onClick(int position) {

        }

        public void update(MockSchool school) {
            nameSchoolTextview.setText(school.getName());
            createdYearTextview.setText(String.valueOf(school.getYear()));
            rebindProgress(school.getSubCriterias());
        }

        private void rebindProgress(@NonNull List<MockSubCriteria> subCriterias) {
            int totalQuestions = subCriterias.size();
            int answeredQuestions = 0;
            for (MockSubCriteria subCriteria : subCriterias) {
                if (subCriteria.getState() != MockSubCriteria.State.NOT_ANSWERED)
                    answeredQuestions++;
            }
            progressTextView.setText(String.format(Locale.US, "%d/%d", answeredQuestions, totalQuestions));

            int progress = totalQuestions > 0 ? (int) ((float) answeredQuestions / totalQuestions * 100) : 0;

            if (Build.VERSION.SDK_INT > 24) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }

            if (progress == 100) {
                int doneColor = progressBar.getResources().getColor(R.color.color_criteria_progress_done);
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(doneColor, android.graphics.PorterDuff.Mode.SRC_IN);
                progressBar.setProgressDrawable(progressDrawable);
                progressTextView.setTextColor(doneColor);
            }
        }

    }

    public interface Callback {
        void onSchoolClicked(School school);
    }
}