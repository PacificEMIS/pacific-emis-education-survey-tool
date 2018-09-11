package fm.doe.national.ui.custom_views.summary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class SummaryView extends LinearLayout {

    private final SummaryCategoryAdapter adapter = new SummaryCategoryAdapter();

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
        int whiteColor = getContext().getResources().getColor(R.color.white);
        nameTextView.setText(null);
        nameTextView.setBackgroundColor(whiteColor);

        firstCellTextView.setText(R.string.first_criteria);
        firstCellTextView.setBackgroundColor(whiteColor);

        secondCellTextView.setText(R.string.second_criteria);
        secondCellTextView.setBackgroundColor(whiteColor);

        thirdCellTextView.setText(R.string.third_criteria);
        thirdCellTextView.setBackgroundColor(whiteColor);

        forthCellTextView.setText(R.string.forth_criteria);
        forthCellTextView.setBackgroundColor(whiteColor);
    }
}
