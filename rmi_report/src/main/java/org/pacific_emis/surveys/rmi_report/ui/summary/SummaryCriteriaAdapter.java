package org.pacific_emis.surveys.rmi_report.ui.summary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import org.pacific_emis.surveys.rmi_report.R;
import org.pacific_emis.surveys.report_core.model.SummaryViewData;

import java.util.ArrayList;
import java.util.List;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private final int subCriteriaCellWeightShort = getResources().getInteger(R.integer.weight_rmi_summary_criteria_value_small);
        private final int subCriteriaCellWeightLong = getResources().getInteger(R.integer.weight_rmi_summary_criteria_value);

        private TextView titleTextView;
        private TextView totalTextView;
        private List<TextView> cellTextViews = new ArrayList<>();

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_rmi_summary_criteria);
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
            updateLayout(item.getAnswerStates().length == cellsCount, cellsCount);
        }

        private void updateLayout(boolean useLongLayout, int cellsCount) {
            cellTextViews.get(cellsCount - 1).setVisibility(useLongLayout ? View.VISIBLE : View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cellTextViews.get(0).getLayoutParams();
            lp.weight = useLongLayout ? subCriteriaCellWeightShort : subCriteriaCellWeightLong;
            for (View cellView : cellTextViews) {
                cellView.setLayoutParams(lp);
            }
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
