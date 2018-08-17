package fm.doe.national.ui.adapters;

import android.support.annotation.NonNull;
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
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.ui.listeners.SubcriteriaStateChangeListener;
import fm.doe.national.ui.screens.base.BaseRecyclerAdapter;
import fm.doe.national.util.ViewUtils;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder> {

    private List<MockCriteria> items;
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

    public void setCriterias(@NonNull List<MockCriteria> criterias) {
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

    protected class CriteriaViewHolder extends BaseRecyclerAdapter.BaseViewHolder {

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
        }

        @Override
        protected void onClick(int position) {

        }

        protected void bind(MockCriteria criteriaPair) {
            titleTextView.setText(criteriaPair.getName());

            SubCriteriaAdapter adapter = new SubCriteriaAdapter(criteriaPair.getSubcriterias());
            adapter.passSubscribers(subscribers);
            adapter.subscribeOnChanges(() -> rebindProgress(criteriaPair.getSubcriterias(),
                    progressTextView, progressBar));
            subcriteriasRecycler.setAdapter(adapter);

            rebindProgress(criteriaPair.getSubcriterias(), progressTextView, progressBar);

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
    }
}
