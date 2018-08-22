package fm.doe.national.ui.screens.base;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.ui.view_data.SubCriteriaViewData;

/**
 * Created by Alexander Chibirev on 8/16/2018.
 */
public abstract class BaseRecyclerAdapter<VH extends BaseRecyclerAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {

    protected View inflateView(ViewGroup parent, @LayoutRes int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onClick(position);
            }
        }

        protected abstract void onClick(int position);

        protected void rebindProgress(@NonNull List<SubCriteriaViewData> subCriterias, TextView progressTextView,
                                    ProgressBar progressBar) {
            int totalQuestions = subCriterias.size();
            int answeredQuestions = 0;
            for (SubCriteriaViewData subCriteria : subCriterias) {
                if (subCriteria.getAnswer() != Answer.State.NOT_ANSWERED) answeredQuestions++;
            }

            int progress = totalQuestions > 0 ? (int) ((float) answeredQuestions / totalQuestions * 100) : 0;
            Resources resources = progressBar.getResources();
            if (progress == 100) {
                progressBar.setProgressDrawable(resources.getDrawable(R.drawable.progress_bar_states_done));
                progressTextView.setTextColor(resources.getColor(R.color.color_criteria_progress_done));
            } else {
                progressBar.setProgressDrawable(resources.getDrawable(R.drawable.progress_bar_states));
                progressTextView.setTextColor(resources.getColor(R.color.color_criteria_primary_dark));
            }

            if (Build.VERSION.SDK_INT > 24) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }

            progressTextView.setText(resources.getString(R.string.criteria_progress, answeredQuestions, totalQuestions));
        }
    }

}