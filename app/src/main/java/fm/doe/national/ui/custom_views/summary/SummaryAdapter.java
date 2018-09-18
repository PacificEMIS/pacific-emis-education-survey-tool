package fm.doe.national.ui.custom_views.summary;

import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.sticky_header.StickyHeaderAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.Constants;

public class SummaryAdapter extends BaseAdapter<SummaryViewData> implements StickyHeaderAdapter<SummaryAdapter.HeaderViewHolder> {

    @Override
    public long getHeaderId(int i) {
        return getItem(i).category.getId();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return new HeaderViewHolder(viewGroup);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder headerViewHolder, int i) {
        headerViewHolder.bind(getItem(i));
    }

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {
        private static final float BOUNDARY_PASS_PERCENTAGE = 0.5f;

        @BindView(R.id.textview_summary_name)
        TextView nameTextView;

        @BindViews({R.id.textview_cell_1, R.id.textview_cell_2, R.id.textview_cell_3, R.id.textview_cell_4})
        List<TextView> cellTextViews;

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            nameTextView.setText(item.name);

            if (cellTextViews.size() < item.progresses.size())
                throw new IllegalStateException(Constants.Errors.WRONT_SUMMARY_INPUT_PARAMETER);

            for (int i = 0; i < cellTextViews.size(); i++) {
                TextView currentTextView = cellTextViews.get(i);

                boolean isProgressExist = i < item.progresses.size();
                currentTextView.setEnabled(isProgressExist);

                if (!isProgressExist) {
                    continue;
                }

                SummaryViewData.Progress progress = item.progresses.get(i);

                boolean isPassed = (float) progress.completed / progress.total >= BOUNDARY_PASS_PERCENTAGE;
                currentTextView.setSelected(isPassed);
                currentTextView.setText(isPassed ?
                        String.format(getString(R.string.criteria_progress), progress.completed, progress.total) : null);
            }
        }
    }

    class HeaderViewHolder extends ViewHolder {
        @BindView(R.id.textview_header_name)
        TextView headerTextView;

        HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_sticky_header);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            headerTextView.setText(item.category.getName());
        }
    }
}
