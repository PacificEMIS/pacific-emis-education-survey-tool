package fm.doe.national.report_core.ui.summary;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

        for (SummaryViewData item : items) {
            if (item.getCategory() != currentCategory) {
                expandedItems.add(SummaryViewData.categoryOnly(item.getCategory()));
                currentCategory = item.getCategory();
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

        private final TypefaceSpan typefaceSpan = new TypefaceSpan(getString(R.string.font_medium));

        TextView nameTextView;
        RecyclerView recyclerView;
        TextView totalTextView;

        SummaryCriteriaAdapter adapter = new SummaryCriteriaAdapter();

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_standard);
            bindViews();
            recyclerView.setAdapter(adapter);
        }

        private void bindViews() {
            nameTextView = findViewById(R.id.textview_standard_name);
            recyclerView = findViewById(R.id.recyclerview);
            totalTextView = findViewById(R.id.textview_total);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            String standardPrefix = getString(R.string.format_standard, item.getStandard().getSuffix());
            SpannableString spannableString = new SpannableString(standardPrefix + " " + item.getStandard().getTitle());
            spannableString.setSpan(typefaceSpan, 0, standardPrefix.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            nameTextView.setText(spannableString);

            totalTextView.setText(String.valueOf(item.getTotalByStandard()));
            totalTextView.setBackgroundColor(ContextCompat.getColor(getContext(), item.getLevel().getColorRes()));

            adapter.setItems(item.getCriteriaSummaryViewDataList());
        }
    }

    class StickyViewHolder extends ViewHolder {

        View fifthSubCriteriaRowView;

        StickyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_top_header);
            bindViews();
        }

        private void bindViews() {
            fifthSubCriteriaRowView = findViewById(R.id.textview_subcriteria_5);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            switch (item.getCategory().getEvaluationForm()) {
                case CLASSROOM_OBSERVATION:
                    fifthSubCriteriaRowView.setVisibility(View.VISIBLE);
                    break;
                case SCHOOL_EVALUATION:
                    fifthSubCriteriaRowView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    class HeaderViewHolder extends ViewHolder {

        TextView headerTextView;

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