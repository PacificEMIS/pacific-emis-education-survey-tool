package fm.doe.national.ui.custom_views.summary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class SummaryView extends LinearLayout {

    private final SummaryAdapter adapter = new SummaryAdapter();

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
        summaryRecyclerView.setAdapter(adapter);
        summaryRecyclerView.setNestedScrollingEnabled(true);
        summaryRecyclerView.setHasFixedSize(true);
    }

    public void setData(List<SummaryViewData> data) {
        adapter.setItems(data);
    }
}
