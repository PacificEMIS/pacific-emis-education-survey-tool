package fm.doe.national.ui.screens.report;

import androidx.annotation.Nullable;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ReportView extends BaseView {

    void setSchoolId(String id);

    void setSchoolName(String name);

    void setPrincipalName(@Nullable String name);

    void setDateOfAccreditation(Date date);

    void setLegend(List<ReportLevel> levels);

}
