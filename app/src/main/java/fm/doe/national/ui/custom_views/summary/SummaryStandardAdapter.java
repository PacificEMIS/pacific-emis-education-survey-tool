package fm.doe.national.ui.custom_views.summary;

import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.Constants;

public class SummaryStandardAdapter extends BaseAdapter<SummaryViewData.Standard> {
    @Override
    protected StandardViewHolder provideViewHolder(ViewGroup parent) {
        return new StandardViewHolder(parent);
    }

    class StandardViewHolder extends ViewHolder {

        private static final int BOUNDARY_PASS = 2;

        private final int nameColor;
        private final int cellColor;
        private final int emptyColor;

        @BindView(R.id.textview_summary_name)
        TextView nameTextView;

        @BindViews({R.id.textview_c1, R.id.textview_c2, R.id.textview_c3, R.id.textview_c4})
        List<TextView> cellTextViews;

        StandardViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary);
            nameColor = getResources().getColor(R.color.color_criteria_primary_dark);
            cellColor = getResources().getColor(R.color.color_summary);
            emptyColor = getResources().getColor(R.color.white);
        }

        @Override
        protected void onBind(SummaryViewData.Standard item) {
            nameTextView.setText(item.name);
            nameTextView.setTextColor(nameColor);
            nameTextView.setBackgroundColor(emptyColor);

            if (cellTextViews.size() < item.progresses.size())
                throw new IllegalStateException(Constants.Errors.WRONT_SUMMARY_INPUT_PARAMETER);

            int i = 0;
            for (; i < item.progresses.size(); i++) {
                TextView currentTextView = cellTextViews.get(i);
                SummaryViewData.Standard.Progress progress = item.progresses.get(i);
                currentTextView.setBackgroundColor(cellColor);

                boolean isPassed = progress.completed >= BOUNDARY_PASS;
                currentTextView.setActivated(isPassed);
                currentTextView.setText(isPassed ?
                        String.format(getString(R.string.criteria_progress), progress.completed, progress.total) : null);
            }
            for (; i < cellTextViews.size(); i++) {
                cellTextViews.get(i).setBackgroundColor(emptyColor);
            }
        }
    }
}
