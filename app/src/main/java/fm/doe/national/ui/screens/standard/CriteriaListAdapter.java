package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class CriteriaListAdapter extends BaseAdapter<Criteria> {

    @Nullable
    private SubcriteriaCallback callback = null;

    public void setCallback(@Nullable SubcriteriaCallback callback) {
        this.callback = callback;
    }

    @Override
    protected CriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new CriteriaViewHolder(parent);
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
            titleTextView.setText(item.getName());
            rebindProgress(item.getCategoryProgress());

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
        public void onSubCriteriaAnswerChanged(@NonNull SubCriteria subCriteria, Answer.State previousState) {
            CategoryProgress categoryProgress = getItem().getCategoryProgress();
            categoryProgress.recalculate(previousState, subCriteria.getAnswer().getState());
            rebindProgress(categoryProgress);
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