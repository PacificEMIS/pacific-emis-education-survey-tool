package fm.doe.national.ui.screens.criterias;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.core.data.model.AnswerState;
import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.core.ui.screens.base.BaseAdapter;

public class CriteriaListAdapter extends BaseAdapter<Criteria> {

    @Nullable
    private SubcriteriaCallback callback = null;

    private final Map<CriteriaViewHolder, Long> viewHolders = new HashMap<>();
    private Set<Long> expandedCriteriaIds = new HashSet<>();

    public void setCallback(@Nullable SubcriteriaCallback callback) {
        this.callback = callback;
    }

    @Override
    protected CriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new CriteriaViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        viewHolders.put((CriteriaViewHolder) holder, getItem(position).getId());
    }

    public void notify(SubCriteria subCriteria) {
        for (Criteria criteria : getItems()) {
            int index = criteria.getSubCriterias().indexOf(subCriteria);
            if (index >= 0) {
                CriteriaViewHolder viewHolder = CollectionUtils.getKeyByValue(viewHolders, criteria.getId());
                if (viewHolder != null) {
                    viewHolder.notify(index);
                    break;
                }
            }
        }
    }

    public void notify(Criteria mutableCriteria) {
        int index = getItems().indexOf(mutableCriteria);
        if (index != RecyclerView.NO_POSITION) {
            notifyItemChanged(index);
        }
    }

    protected class CriteriaViewHolder extends ViewHolder implements SubCriteriaListAdapter.OnAnswerStateChangedListener {

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

        private final SubCriteriaListAdapter adapter = new SubCriteriaListAdapter();

        CriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_question_header);
            adapter.setCallback(callback);
            adapter.setAnswerStateChangedListener(this);
            subcriteriasRecycler.setAdapter(adapter);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBind(Criteria item) {
            adapter.setItems((List<SubCriteria>) item.getSubCriterias());

            String criteriaPrefix = getString(R.string.format_criteria, item.getSuffix());
            SpannableString spannableString = new SpannableString(criteriaPrefix + " " + item.getTitle());
            spannableString.setSpan(
                    new TypefaceSpan(getString(R.string.font_medium)),
                    0, criteriaPrefix.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            titleTextView.setText(spannableString);

            rebindProgress(item.getProgress());

            if (expandedCriteriaIds.contains(item.getId())) {
                subcriteriasRecycler.setVisibility(View.VISIBLE);
                arrowImageView.setImageResource(R.drawable.ic_expand_less_24dp);
            } else {
                subcriteriasRecycler.setVisibility(View.GONE);
                arrowImageView.setImageResource(R.drawable.ic_expand_more_24dp);
            }

            header.setOnClickListener((View v) -> {
                if (subcriteriasRecycler.getVisibility() == View.VISIBLE) {
                    expandedCriteriaIds.remove(item.getId());
                } else {
                    expandedCriteriaIds.add(item.getId());
                }
                notifyItemChanged(getAdapterPosition());
            });
        }

        @Override
        public void onSubCriteriaAnswerChanged(@NonNull SubCriteria subCriteria, AnswerState answerState) {
            rebindProgress(getItem().getProgress());
        }

        public void notify(int subcriteriaIndex) {
            adapter.notifyItemChanged(subcriteriaIndex);
        }

        private void rebindProgress(Progress progress) {
            ViewUtils.rebindProgress(progress, progressTextView, progressBar);
        }

    }
}