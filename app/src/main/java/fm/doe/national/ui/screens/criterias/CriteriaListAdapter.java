package fm.doe.national.ui.screens.criterias;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.CollectionUtils;
import fm.doe.national.utils.ViewUtils;

public class CriteriaListAdapter extends BaseAdapter<Criteria> {

    @Nullable
    private SubcriteriaCallback callback = null;

    private final Map<CriteriaViewHolder, Long> viewHolders = new HashMap<>();
    private List<Long> expandedCriteriaIds = new ArrayList<>();

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
            super(parent, R.layout.item_criteria);
            adapter.setCallback(callback);
            adapter.setAnswerStateChangedListener(this);
            subcriteriasRecycler.setAdapter(adapter);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBind(Criteria item) {
            adapter.setItems((List<SubCriteria>) item.getSubCriterias());

            String criteriaPrefix = getString(R.string.format_criteria, item.getIndex());
            SpannableString spannableString = new SpannableString(criteriaPrefix + " " + item.getName());
            spannableString.setSpan(
                    new TypefaceSpan(getString(R.string.font_medium)),
                    0, criteriaPrefix.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            titleTextView.setText(spannableString);

            rebindProgress(item.getCategoryProgress());

            // TODO: use AnimatedVectorDrawable to animate arrows sometime later
            if (expandedCriteriaIds.contains(item.getId())) {
                subcriteriasRecycler.setVisibility(View.VISIBLE);
                arrowImageView.setImageResource(R.drawable.ic_criteria_expand_more_24dp);
            } else {
                subcriteriasRecycler.setVisibility(View.GONE);
                arrowImageView.setImageResource(R.drawable.ic_criteria_expand_less_24dp);
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
        public void onSubCriteriaAnswerChanged(@NonNull SubCriteria subCriteria, Answer.State previousState) {
            CategoryProgress categoryProgress = getItem().getCategoryProgress();
            categoryProgress.recalculate(previousState, subCriteria.getAnswer().getState());
            rebindProgress(categoryProgress);
        }

        public void notify(int subcriteriaIndex) {
            adapter.notifyItemChanged(subcriteriaIndex);
        }

        private void rebindProgress(CategoryProgress progress) {
            ViewUtils.rebindProgress(
                    progress.getTotalQuestionsCount(),
                    progress.getAnsweredQuestionsCount(),
                    getString(R.string.criteria_progress),
                    progressTextView,
                    progressBar);
        }

    }
}