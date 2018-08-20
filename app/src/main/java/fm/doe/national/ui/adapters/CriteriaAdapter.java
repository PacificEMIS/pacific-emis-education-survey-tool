package fm.doe.national.ui.adapters;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.listeners.SubcriteriaStateChangeListener;
import fm.doe.national.ui.view_data.CriteriaViewData;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
import fm.doe.national.utils.ViewUtils;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder> {

    private List<CriteriaViewData> items;
    private List<SubcriteriaStateChangeListener> subscribers;

    public CriteriaAdapter() {
        super();
        items = new ArrayList<>();
        subscribers = new ArrayList<>();
    }

    @NonNull
    @Override
    public CriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CriteriaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_criteria, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setCriterias(@NonNull List<CriteriaViewData> criterias) {
        items = criterias;
        notifyDataSetChanged();
    }

    public void flushSubscribers() {
        subscribers.clear();
    }

    public void unsubscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.remove(listener);
    }

    public void subscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.add(listener);
    }

    protected class CriteriaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_criteria_title)
        TextView titleTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        @BindView(R.id.recyclerview_subcriterias)
        OmegaRecyclerView subcriteriasRecycler;

        @BindView(R.id.constraintlayout_criteria_header)
        View header;

        @BindView(R.id.image_expanding_arrow)
        ImageView arrowImageView;

        protected CriteriaViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        protected void bind(CriteriaViewData criteria) {
            titleTextView.setText(criteria.getName());

            SubCriteriaAdapter adapter = new SubCriteriaAdapter(criteria.getQuestionsViewData());
            adapter.passSubscribers(subscribers);
            adapter.subscribeOnChanges((@NonNull SubCriteriaViewData subCriteriaViewData,
                                        @NonNull SubCriteria subCriteria,
                                        @Nullable Answer answer,
                                        Answer.State newState) ->
                    rebindProgress(criteria.getQuestionsViewData())
            );
            subcriteriasRecycler.setAdapter(adapter);

            rebindProgress(criteria.getQuestionsViewData());

            // TODO: use AnimatedVectorDrawable to animate arrows
            header.setOnClickListener((View v) -> {
                if (subcriteriasRecycler.getVisibility() == View.VISIBLE) {
                    ViewUtils.animateCollapsing(subcriteriasRecycler);
                    arrowImageView.setImageResource(R.drawable.ic_criteria_expand_less_24dp);
                } else {
                    ViewUtils.animateExpanding(subcriteriasRecycler);
                    arrowImageView.setImageResource(R.drawable.ic_criteria_expand_more_24dp);
                }
            });
        }

        private void rebindProgress(@NonNull List<SubCriteriaViewData> subCriterias) {
            int totalQuestions = subCriterias.size();
            int answeredQuestions = 0;
            for (SubCriteriaViewData subCriteria: subCriterias) {
                if (subCriteria.getAnswer() != Answer.State.NOT_ANSWERED) answeredQuestions++;
            }
            progressTextView.setText(String.format(Locale.US, "%d/%d", answeredQuestions, totalQuestions));

            int progress = totalQuestions > 0 ? (int)((float)answeredQuestions / totalQuestions * 100) : 0;

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
            } else {
                // TODO: fix progress bar
            }
        }
    }
}
