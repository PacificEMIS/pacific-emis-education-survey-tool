package org.pacific_emis.surveys.rmi_report.ui.summary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.sticky_decoration.StickyAdapter;

import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.EvaluationForm;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.report_core.model.Level;
import org.pacific_emis.surveys.report_core.model.SummaryViewData;
import org.pacific_emis.surveys.rmi_report.R;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SummaryStandardAdapter extends BaseAdapter<SummaryViewData> implements StickyAdapter<SummaryStandardAdapter.StickyViewHolder> {

    private static final int ITEM_VIEW_TYPE_TITLE = 0;
    private static final int ITEM_VIEW_TYPE_STANDARD = 1;

    @Override
    public long getStickyId(int i) {
        return getItem(i).getCategory().getEvaluationForm().ordinal();
    }

    @Override
    public StickyViewHolder onCreateStickyViewHolder(ViewGroup viewGroup) {
        return new StickyViewHolder(viewGroup);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isCategoryOnly() ? ITEM_VIEW_TYPE_TITLE : ITEM_VIEW_TYPE_STANDARD;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_TITLE:
                return provideHeaderViewHolder(parent);
            case ITEM_VIEW_TYPE_STANDARD:
                return provideViewHolder(parent);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void setItems(@NonNull List<SummaryViewData> items) {
        List<SummaryViewData> expandedItems = new ArrayList<>();
        Category currentCategory = null;
        SummaryViewData currentCategoryViewData = null;

        for (SummaryViewData item : items) {
            Category category = item.getCategory();
            if (category != currentCategory) {
                currentCategory = category;
                currentCategoryViewData = SummaryViewData.categoryOnly(currentCategory, item.getLayoutType());
                expandedItems.add(currentCategoryViewData);
            }
            expandedItems.add(item);
        }
        super.setItems(expandedItems);
    }

    @Override
    public void onBindStickyViewHolder(StickyViewHolder headerViewHolder, int i) {
        headerViewHolder.bind(getItem(i));
    }

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    private HeaderViewHolder provideHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private TextView nameTextView;
        private TextView titleTextView;
        private RecyclerView recyclerView;
        private TextView totalTextView;
        private TextView levelTextView;
        private View delimeterView;

        private View totalLayout;
        private TextView totalValueTextView;
        private TextView totalLevelTextView;

        SummaryCriteriaAdapter adapter = new SummaryCriteriaAdapter();

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_rmi_summary_standard);
            bindViews();
            recyclerView.setAdapter(adapter);
        }

        private void bindViews() {
            titleTextView = findViewById(R.id.textview_standard_title);
            nameTextView = findViewById(R.id.textview_standard_name);
            recyclerView = findViewById(R.id.recyclerview);
            totalTextView = findViewById(R.id.textview_total);
            levelTextView = findViewById(R.id.textview_level);
            delimeterView = findViewById(R.id.view_delimeter);

            totalLayout = findViewById(R.id.layout_total);
            totalValueTextView = findViewById(R.id.textview_total_value);
            totalLevelTextView = findViewById(R.id.textview_total_level);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            titleTextView.setText(getString(R.string.format_standard, item.getStandard().getSuffix()));
            nameTextView.setText(item.getStandard().getTitle());

            totalTextView.setText(String.valueOf(item.getTotalByStandard()));

            levelTextView.setText(String.valueOf(item.getLevel().getValue()));
            ViewUtils.setTintedBackgroundDrawable(levelTextView, R.drawable.bg_level, item.getLevel().getColorRes());

            adapter.setItems(item.getCriteriaSummaryViewDataList());

            delimeterView.setVisibility(shouldHideBottomDelimeter() ? View.GONE : View.VISIBLE);

            if (item.getCategory().getEvaluationForm() == EvaluationForm.CLASSROOM_OBSERVATION && shouldHideBottomDelimeter()) {
                RmiReportLevel totalLevel = RmiReportLevel.estimateLevel(item.getTotalByCategory(), 0);

                totalLayout.setVisibility(View.VISIBLE);
                totalValueTextView.setText(String.valueOf(item.getTotalByCategory()));
                totalLevelTextView.setText(String.valueOf(totalLevel.getValue()));
                ViewUtils.setTintedBackgroundDrawable(totalLevelTextView, R.drawable.bg_level, totalLevel.getColorRes());
            } else {
                totalLayout.setVisibility(View.GONE);
            }
        }

        private boolean shouldHideBottomDelimeter() {
            int position = getAdapterPosition();
            long currentGroupId = getStickyId(position);

            if (position == RecyclerView.NO_POSITION) {
                return false;
            }

            if (position == getItemCount() - 1) {
                return true;
            }

            if (position >= getItemCount() - 1) {
                return false;
            }

            long nextItemGroupId = getStickyId(position + 1);

            if (nextItemGroupId != currentGroupId) {
                return false;
            }

            return SummaryStandardAdapter.this.getItem(position + 1).isCategoryOnly();
        }
    }

    class StickyViewHolder extends ViewHolder {

        private final int subCriteriaRowWeightShort = getResources().getInteger(R.integer.weight_rmi_summary_criteria_value_small);
        private final int subCriteriaRowWeightLong = getResources().getInteger(R.integer.weight_rmi_summary_criteria_value);
        private final int[] subCriteriaIds = new int[] {
                R.id.textview_subcriteria_1,
                R.id.textview_subcriteria_2,
                R.id.textview_subcriteria_3,
                R.id.textview_subcriteria_4,
                R.id.textview_subcriteria_5
        };

        private final List<View> subCriteriaRowViews = new ArrayList<>();

        StickyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_rmi_summary_top_header);
            bindViews();
        }

        private void bindViews() {
            for (int subCriteriaId : subCriteriaIds) {
                subCriteriaRowViews.add(findViewById(subCriteriaId));
            }
        }

        @Override
        protected void onBind(SummaryViewData item) {
            int subCriteriaRowCount = subCriteriaRowViews.size();
            updateLayout(item.getLayoutType() == SummaryViewData.LayoutType.LONG, subCriteriaRowCount);
        }

        private void updateLayout(boolean useLongLayout, int subCriteriaRowCount) {
            subCriteriaRowViews.get(subCriteriaRowCount - 1).setVisibility(useLongLayout ? View.VISIBLE : View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) subCriteriaRowViews.get(0).getLayoutParams();
            lp.weight = useLongLayout ? subCriteriaRowWeightShort : subCriteriaRowWeightLong;
            for (View subCriteriaView : subCriteriaRowViews) {
                subCriteriaView.setLayoutParams(lp);
            }
        }
    }

    class HeaderViewHolder extends ViewHolder {

        private TextView headerTextView = findViewById(R.id.textview_header_name);

        HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_rmi_summary_sticky_header);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            item.getCategory().getEvaluationForm().getName().applyTo(headerTextView);
        }
    }
}
