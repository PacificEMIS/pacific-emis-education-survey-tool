package fm.doe.national.ui.screens.report.summary;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.sticky_header.StickyHeaderAdapter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class SummaryStandardAdapter extends BaseAdapter<SummaryViewData> implements StickyHeaderAdapter<SummaryStandardAdapter.HeaderViewHolder> {

    @Override
    public long getHeaderId(int i) {
        return getItem(i).getCategory().getId();
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

        @BindView(R.id.textview_standard_name)
        TextView nameTextView;

        @BindView(R.id.recyclerview)
        RecyclerView recyclerView;

        @BindView(R.id.textview_total)
        TextView totalTextView;

        SummaryCriteriaAdapter adapter = new SummaryCriteriaAdapter();

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_summary_standard);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onBind(SummaryViewData item) {
            String standardPrefix = getString(R.string.format_standard, item.getStandard().getSuffix());
            SpannableString spannableString = new SpannableString(standardPrefix + " " + item.getStandard().getTitle());
            spannableString.setSpan(
                    new TypefaceSpan(getString(R.string.font_medium)),
                    0, standardPrefix.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            nameTextView.setText(spannableString);

            totalTextView.setText(String.valueOf(item.getTotalByStandard()));

            adapter.setItems(item.getCriteriaSummaryViewDataList());
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
            headerTextView.setText(item.getCategory().getTitle());
        }
    }
}
