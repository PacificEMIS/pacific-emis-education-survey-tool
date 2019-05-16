package fm.doe.national.ui.screens.report.summary;

import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class SummaryCriteriaAdapter extends BaseListAdapter<SummaryViewData.CriteriaSummaryViewData> {

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.textview_criteria_title)
        TextView titleTextView;

        @BindView(R.id.textview_total)
        TextView totalTextView;

        @BindViews({R.id.textview_cell_1, R.id.textview_cell_2, R.id.textview_cell_3, R.id.textview_cell_4})
        List<SummaryTextView> cellTextViews;

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_criteria);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBind(SummaryViewData.CriteriaSummaryViewData item) {
            titleTextView.setText(item.getCriteriaTitle());
            totalTextView.setText(String.valueOf(item.getTotal()));

            for (int i = 0; i < cellTextViews.size(); i++) {
                cellTextViews.get(i).setText(safeGetAnswerState(i));
            }
        }

        @NotNull
        private String safeGetAnswerState(int index) {
            if (index < getItem().getAnswerStates().length) {
                return getItem().getAnswerStates()[index] ? "1" : "0";
            } else {
                return "0";
            }
        }

    }
}
