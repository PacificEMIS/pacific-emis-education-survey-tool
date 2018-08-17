package fm.doe.national.ui.screens.base;

import android.graphics.drawable.Drawable;
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
import java.util.Locale;

import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.mock.MockSubCriteria;

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

        protected void rebindProgress(@NonNull List<MockSubCriteria> subCriterias,
                                      TextView progressTextView,
                                      ProgressBar progressBar) {
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
}