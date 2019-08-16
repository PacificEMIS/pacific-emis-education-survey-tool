package fm.doe.national.report_core.ui.summary;

import androidx.annotation.NonNull;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.ui.base.BaseReportView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SummaryView extends BaseReportView {

    void setLoadingVisibility(boolean visible);

    void setSummaryData(@NonNull List<SummaryViewData> data);

}