package fm.doe.national.ui.screens.report;

import androidx.annotation.NonNull;

import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.report.summary.SummaryView;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;

public class ReportActivity extends BaseActivity implements ReportView {

    @BindView(R.id.summaryview)
    SummaryView summaryView;

    @InjectPresenter
    ReportPresenter presenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }

    @Override
    public void showSummaryLoading() {
        // nothing
    }

    @Override
    public void hideSummaryLoading() {
        // nothing
    }

    @Override
    public void setSummaryData(@NonNull List<SummaryViewData> data) {
        summaryView.setData(data);
    }
}
