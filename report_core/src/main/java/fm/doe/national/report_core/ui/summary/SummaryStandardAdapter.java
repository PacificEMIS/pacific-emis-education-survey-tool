package fm.doe.national.report_core.ui.summary;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.sticky_decoration.StickyAdapter;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.report_core.R;
import fm.doe.national.report_core.model.SummaryViewData;

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
        private View delimeterView;

        SummaryCriteriaAdapter adapter = new SummaryCriteriaAdapter();

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_standard);
            bindViews();
            recyclerView.setAdapter(adapter);
        }

        private void bindViews() {
            titleTextView = findViewById(R.id.textview_standard_title);
            nameTextView = findViewById(R.id.textview_standard_name);
            recyclerView = findViewById(R.id.recyclerview);
            totalTextView = findViewById(R.id.textview_total);
            delimeterView = findViewById(R.id.view_delimeter);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            titleTextView.setText(getString(R.string.format_standard, item.getStandard().getSuffix()));
            nameTextView.setText(item.getStandard().getTitle());

            totalTextView.setText(String.valueOf(item.getTotalByStandard()));

            Drawable backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_level);

            if (backgroundDrawable != null) {
                backgroundDrawable = DrawableCompat.wrap(backgroundDrawable);
                DrawableCompat.setTint(
                        backgroundDrawable.mutate(),
                        ContextCompat.getColor(getContext(), item.getLevel().getColorRes())
                );
                totalTextView.setBackground(backgroundDrawable);
            }

            adapter.setItems(item.getCriteriaSummaryViewDataList());

            delimeterView.setVisibility(shouldHideBottomDelimeter() ? View.GONE : View.VISIBLE);
        }

        private boolean shouldHideBottomDelimeter() {
            int position = getAdapterPosition();
            long currentGroupId = getStickyId(position);

            if (position == RecyclerView.NO_POSITION) {
                return false;
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

        private final int subCriteriaTotalWeightShort = getResources().getInteger(R.integer.weight_summary_criteria_total_small);
        private final int subCriteriaTotalWeightLong = getResources().getInteger(R.integer.weight_summary_criteria_total);

        private View fifthSubCriteriaRowView;
        private View subCriteriaTotalView;

        StickyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_top_header);
            bindViews();
        }

        private void bindViews() {
            fifthSubCriteriaRowView = findViewById(R.id.textview_subcriteria_5);
            subCriteriaTotalView = findViewById(R.id.layout_subcriteria_total);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            updateLayout(item.getLayoutType() == SummaryViewData.LayoutType.LONG);
        }

        private void updateLayout(boolean useLongLayout) {
            fifthSubCriteriaRowView.setVisibility(useLongLayout ? View.VISIBLE : View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) subCriteriaTotalView.getLayoutParams();
            lp.weight = useLongLayout ? subCriteriaTotalWeightShort : subCriteriaTotalWeightLong;
            subCriteriaTotalView.setLayoutParams(lp);
            subCriteriaTotalView.requestLayout();
        }
    }

    class HeaderViewHolder extends ViewHolder {

        private TextView headerTextView;

        HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_sticky_header);
            bindViews();
        }

        private void bindViews() {
            headerTextView = findViewById(R.id.textview_header_name);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            headerTextView.setText(item.getCategory().getTitle());
        }
    }
}
