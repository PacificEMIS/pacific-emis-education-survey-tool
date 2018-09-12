package fm.doe.national.ui.custom_views.summary;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.utils.Constants;

public class SummaryView extends LinearLayout {

    private final SummaryCategoryAdapter adapter = new SummaryCategoryAdapter();
    private final int[] titles = new int[] {
            R.string.first_criteria,
            R.string.second_criteria,
            R.string.third_criteria,
            R.string.forth_criteria
    };

    @BindView(R.id.textview_summary_name)
    TextView nameTextView;

    @BindViews({ R.id.textview_c1, R.id.textview_c2, R.id.textview_c3, R.id.textview_c4 })
    List<TextView> cellTextViews;

    @BindView(R.id.recyclerview_summary)
    RecyclerView summaryRecyclerView;

    public SummaryView(Context context) {
        this(context, null);
    }

    public SummaryView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SummaryView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        inflate(context, R.layout.view_summary, this);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Resources resources = getContext().getResources();
        int whiteColor = resources.getColor(R.color.white);
        int blackColor = resources.getColor(R.color.black);

        nameTextView.setText(null);
        nameTextView.setBackgroundColor(whiteColor);

        if (cellTextViews.size() != titles.length) throw new RuntimeException(Constants.Errors.SUMMARY_COLUMNS_MISMATCH);

        for (int i = 0; i < cellTextViews.size(); i++) {
            TextView textView = cellTextViews.get(i);
            int textRes = titles[i];
            textView.setText(textRes);
            textView.setBackgroundColor(whiteColor);
            textView.setTextColor(blackColor);
        }

        summaryRecyclerView.setAdapter(adapter);
    }

    public void setData(List<SummaryViewData> data) {
        adapter.setItems(data);
    }
}
