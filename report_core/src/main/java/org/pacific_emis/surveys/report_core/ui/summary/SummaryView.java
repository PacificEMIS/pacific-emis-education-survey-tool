package org.pacific_emis.surveys.report_core.ui.summary;

import androidx.annotation.NonNull;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.report_core.model.SummaryViewData;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportView;

public interface SummaryView extends BaseReportView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLoadingVisibility(boolean visible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSummaryData(@NonNull List<SummaryViewData> data);

}
