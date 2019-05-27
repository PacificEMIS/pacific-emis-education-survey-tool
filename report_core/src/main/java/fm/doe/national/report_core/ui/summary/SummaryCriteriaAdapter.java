package fm.doe.national.report_core.ui.summary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.report_core.R;
import fm.doe.national.report_core.model.SummaryViewData;

public class SummaryCriteriaAdapter extends BaseListAdapter<SummaryViewData.CriteriaSummaryViewData> {

    private static final String MARK_ANSWERED = "1";
    private static final String MARK_NOT_ANSWERED = "0";
    private static final int[] cellIds = new int[] {
            R.id.textview_cell_1,
            R.id.textview_cell_2,
            R.id.textview_cell_3,
            R.id.textview_cell_4,
            R.id.textview_cell_5
    };

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        TextView titleTextView;
        TextView totalTextView;
        List<TextView> cellTextViews = new ArrayList<>();

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_criteria);
            bindViews();
        }

        private void bindViews() {
            titleTextView = findViewById(R.id.textview_criteria_title);
            totalTextView = findViewById(R.id.textview_total);
            for (int cellId : cellIds) {
                cellTextViews.add(findViewById(cellId));
            }
        }

        @Override
        protected void onBind(SummaryViewData.CriteriaSummaryViewData item) {
            titleTextView.setText(item.getCriteriaTitle());
            totalTextView.setText(String.valueOf(item.getTotal()));

            for (int i = 0; i < cellTextViews.size(); i++) {
                cellTextViews.get(i).setText(isAnsweredAt(i) ? MARK_ANSWERED : MARK_NOT_ANSWERED);
            }

            int cellsCount = cellTextViews.size();
            cellTextViews.get(cellsCount - 1).setVisibility(item.getAnswerStates().length < cellsCount ? View.GONE : View.VISIBLE);
        }

        private boolean isAnsweredAt(int index) {
            if (index < getItem().getAnswerStates().length) {
                return getItem().getAnswerStates()[index];
            } else {
                return false;
            }
        }

    }
}
