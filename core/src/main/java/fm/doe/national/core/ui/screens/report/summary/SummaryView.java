package fm.doe.national.core.ui.screens.report.summary;

import androidx.annotation.NonNull;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.SummaryViewData;
import fm.doe.national.core.ui.screens.report.base.BaseReportView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SummaryView extends BaseReportView {

    void setLoadingVisibility(boolean visible);

    void setSummaryData(@NonNull List<SummaryViewData> data);

}
