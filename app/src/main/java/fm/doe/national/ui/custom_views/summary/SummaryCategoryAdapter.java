package fm.doe.national.ui.custom_views.summary;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class SummaryCategoryAdapter extends BaseAdapter<SummaryViewData> {

    @Override
    protected SummaryViewHolder provideViewHolder(ViewGroup parent) {
        return new SummaryViewHolder(parent);
    }

    class SummaryViewHolder extends ViewHolder {

        private final SummaryCategoryAdapter adapter = new SummaryCategoryAdapter();
        private final int headerColor;

        @BindView(R.id.textview_summary_name)
        TextView nameTextView;

        @BindView(R.id.textview_c1)
        TextView firstCellTextView;

        @BindView(R.id.textview_c2)
        TextView secondCellTextView;

        @BindView(R.id.textview_c3)
        TextView thirdCellTextView;

        @BindView(R.id.textview_c4)
        TextView forthCellTextView;

        @BindView(R.id.recyclerview_summary)
        RecyclerView summaryRecyclerView;

        SummaryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_category);
            headerColor = getContext().getResources().getColor(R.color.color_criteria_progress_bg);
            summaryRecyclerView.setAdapter(adapter);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            nameTextView.setText(item.category);
            nameTextView.setBackgroundColor(headerColor);
            firstCellTextView.setBackgroundColor(headerColor);
            secondCellTextView.setBackgroundColor(headerColor);
            thirdCellTextView.setBackgroundColor(headerColor);
            forthCellTextView.setBackgroundColor(headerColor);
            
        }
    }
}
