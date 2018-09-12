package fm.doe.national.ui.custom_views.summary;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class SummaryCategoryAdapter extends BaseAdapter<SummaryViewData> {

    @Override
    protected SummaryViewHolder provideViewHolder(ViewGroup parent) {
        return new SummaryViewHolder(parent);
    }

    class SummaryViewHolder extends ViewHolder {

        private final SummaryStandardAdapter adapter = new SummaryStandardAdapter();
        private final int headerColor;

        @BindView(R.id.textview_summary_name)
        TextView nameTextView;

        @BindViews({ R.id.textview_c1, R.id.textview_c2, R.id.textview_c3, R.id.textview_c4 })
        List<TextView> cellTextViews;

        @BindView(R.id.recyclerview_summary)
        RecyclerView summaryRecyclerView;

        SummaryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_category);
            headerColor = getResources().getColor(R.color.color_criteria_progress_bg);
            summaryRecyclerView.setAdapter(adapter);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            nameTextView.setText(item.category);
            nameTextView.setBackgroundColor(headerColor);
            nameTextView.setAllCaps(true);
            nameTextView.setTextSize(10);
            for (TextView textView : cellTextViews) {
                textView.setBackgroundColor(headerColor);
            }
            adapter.setItems(item.standards);
        }
    }
}
