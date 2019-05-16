package fm.doe.national.ui.screens.report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.report.recommendations.Recommendation;
import fm.doe.national.ui.screens.report.recommendations.RecommendationsAdapter;
import fm.doe.national.ui.screens.report.summary.SummaryView;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;

public class ReportActivity extends BaseActivity implements ReportView {

    @BindView(R.id.summaryview)
    SummaryView summaryView;

    @BindView(R.id.recyclerview_recommendations)
    RecyclerView recommendationsRecyclerView;

    private RecommendationsAdapter recommendationsAdapter = new RecommendationsAdapter();

    @InjectPresenter
    ReportPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendationsRecyclerView.setAdapter(recommendationsAdapter);
        recommendationsRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }

    @Override
    public void setSummaryLoadingVisibility(boolean visible) {
        // nothing
    }

    @Override
    public void setSummaryData(@NonNull List<SummaryViewData> data) {
        summaryView.setData(data);
    }

    @Override
    public void setRecommendationsLoadingVisibility(boolean visible) {
        // nothing
    }

    @Override
    public void setRecommendations(List<Recommendation> recommendations) {
        recommendationsAdapter.setItems(recommendations);
    }
}
