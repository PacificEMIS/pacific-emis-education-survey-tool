package fm.doe.national.ui.custom_views.summary;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.sticky_header.StickyHeaderAdapter;

import java.util.ArrayList;
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

        @BindView(R.id.textview_summary_name)
        TextView nameTextView;

        @BindViews({R.id.textview_cell_1, R.id.textview_cell_2, R.id.textview_cell_3, R.id.textview_cell_4})
        List<SummaryTextView> cellTextViews;

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            String criteriaPrefix = getString(R.string.format_criteria, item.standard.getIndex());
            SpannableString spannableString = new SpannableString(criteriaPrefix + " " + item.standard.getName());
            spannableString.setSpan(
                    new TypefaceSpan(getString(R.string.font_medium)),
                    0, criteriaPrefix.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            nameTextView.setText(spannableString);

            int progressesSize = item.progresses.size();

            if (cellTextViews.size() < progressesSize)
                throw new IllegalStateException(Constants.Errors.WRONT_SUMMARY_INPUT_PARAMETER);

            for (int i = 0; i < cellTextViews.size(); i++) {
                SummaryTextView currentTextView = cellTextViews.get(i);

                boolean isProgressExist = i < progressesSize;

                if (!isProgressExist) {
                    continue;
                }

                SummaryViewData.Progress progress = item.progresses.get(i);

                currentTextView.setAnswersCount(progress.total, SummaryTextView.AnswerType.MAX);
                currentTextView.setAnswersCount(progress.positive, SummaryTextView.AnswerType.POSITIVE);
                currentTextView.setAnswersCount(progress.negative, SummaryTextView.AnswerType.NEGATIVE);

                // Clients want to make text progress temporary invisible
//                boolean isPassed = (float) completedCount / totalCount >= BOUNDARY_PASS_PERCENTAGE;
//                currentTextView.setSelected(isPassed);
//                currentTextView.setText(isPassed ?
//                        String.format(getString(R.string.criteria_progress), completedCount, totalCount) : null);
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
