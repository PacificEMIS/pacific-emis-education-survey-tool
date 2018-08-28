package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;
import fm.doe.national.ui.view_data.CriteriaViewData;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
import fm.doe.national.utils.ViewUtils;

public class CriteriaAdapter extends BaseClickableAdapter<CriteriaViewData, CriteriaAdapter.CriteriaViewHolder> {

    private List<SubcriteriaStateChangeListener> subscribers = new ArrayList<>();

    public void clearSubscribers() {
        subscribers.clear();
    }

    public void unsubscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.remove(listener);
    }

    public void subscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.add(listener);
    }

    @Override
    protected CriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new CriteriaViewHolder(parent);
    }

    protected class CriteriaViewHolder
            extends BaseRecyclerViewHolder<CriteriaViewData> implements SubcriteriaStateChangeListener {

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

        CriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_criteria);

            adapter.addSubscribers(subscribers);
            adapter.subscribeOnChanges(this);
            subcriteriasRecycler.setAdapter(adapter);
        }

        @Override
        public void onBind() {
            adapter.setItems(item.getQuestionsViewData());
            titleTextView.setText(item.getName());
            rebindProgress(item.getQuestionsViewData().size(), item.getAnsweredCount(), progressTextView, progressBar);
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
            rebindProgress(item.getQuestionsViewData().size(), item.getAnsweredCount(), progressTextView, progressBar);
        }
    }
}