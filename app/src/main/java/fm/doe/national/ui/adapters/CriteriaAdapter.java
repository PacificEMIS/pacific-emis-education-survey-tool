package fm.doe.national.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

    private List<CriteriaViewData> items = new ArrayList<>();
    private List<SubcriteriaStateChangeListener> subscribers = new ArrayList<>();

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

    @NonNull
    @Override
    public CriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CriteriaViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
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

        private final SubCriteriaAdapter adapter = new SubCriteriaAdapter();
        private CriteriaViewData criteria;
        private SubCriteriaAdapter subCriteriaAdapter;

        CriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_criteria);

            adapter.addSubscribers(subscribers);
            adapter.subscribeOnChanges(this);
        }

        void bind(@NonNull CriteriaViewData criteriaViewData) {
            criteria = criteriaViewData;
            subCriteriaAdapter.setSubCriterias(criteria.getQuestionsViewData());
            titleTextView.setText(criteria.getName());
            rebindProgress(criteria.getQuestionsViewData().size(), criteria.getAnsweredCount(), progressTextView, progressBar);
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
        public void onSubCriteriaStateChanged(@NonNull SubCriteriaViewData viewData,
                                              @NonNull SubCriteria subCriteria,
                                              @Nullable Answer answer,
                                              Answer.State newState) {
            rebindProgress(criteria.getQuestionsViewData().size(), criteria.getAnsweredCount(), progressTextView, progressBar);
        }
    }
}