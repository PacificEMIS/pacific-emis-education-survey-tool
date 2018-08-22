package fm.doe.national.ui.adapters;

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

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.listeners.SubcriteriaStateChangeListener;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;
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

    public void clearSubscribers() {
        subscribers.clear();
    }

    public void unsubscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.remove(listener);
    }

    public void subscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.add(listener);
    }

    protected class CriteriaViewHolder extends BaseRecyclerViewHolder implements SubcriteriaStateChangeListener {

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

        @BindView(R.id.imageview_expanding_arrow)
        ImageView arrowImageView;

        private CriteriaViewData criteria;
        private SubCriteriaAdapter subCriteriaAdapter;

        public CriteriaViewHolder(View v) {
            super(v);
            SubCriteriaAdapter adapter = new SubCriteriaAdapter();
            adapter.addSubscribers(subscribers);
            adapter.subscribeOnChanges(this);
        }

        protected void bind(@NonNull CriteriaViewData criteriaViewData) {
            criteria = criteriaViewData;
            subCriteriaAdapter.setSubCriterias(criteria.getQuestionsViewData());
            titleTextView.setText(criteria.getName());
            rebindProgress(criteria.getQuestionsViewData());
            // TODO: use AnimatedVectorDrawable to animate arrows sometime later
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

        @Override
        public void onStateChanged(@NonNull SubCriteriaViewData viewData,
                                   @NonNull SubCriteria subCriteria,
                                   @Nullable Answer answer,
                                   Answer.State newState) {
            rebindProgress(criteria.getQuestionsViewData());
        }

        private void rebindProgress(@NonNull List<SubCriteriaViewData> subCriterias) {
            int totalQuestions = subCriterias.size();
            int answeredQuestions = 0;
            for (SubCriteriaViewData subCriteria : subCriterias) {
                if (subCriteria.getAnswer() != Answer.State.NOT_ANSWERED) answeredQuestions++;
            }

            int progress = totalQuestions > 0 ? (int) ((float) answeredQuestions / totalQuestions * 100) : 0;
            if (progress == 100) {
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_states_done));
                progressTextView.setTextColor(getResources().getColor(R.color.color_criteria_progress_done));
            } else {
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_states));
                progressTextView.setTextColor(getResources().getColor(R.color.color_criteria_primary_dark));
            }

            if (Build.VERSION.SDK_INT > 24) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }

            progressTextView.setText(getResources().getString(R.string.criteria_progress, answeredQuestions, totalQuestions));
        }
    }
}