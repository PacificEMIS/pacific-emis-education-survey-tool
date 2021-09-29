package org.pacific_emis.surveys.accreditation.ui.observation_log;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

public interface ObservationLogView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrevButtonVisible(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonEnabled(boolean isEnabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextButtonText(Text text);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateLog(List<MutableObservationLogRecord> items);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateLogScrollingToPosition(List<MutableObservationLogRecord> items, int position);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showTimePicker(@NonNull Date sourceDate, @NonNull OnTimePickedListener listener);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSurveyUploadState(UploadState uploadState);

    interface OnTimePickedListener {
        void onTimePicked(@NonNull Date date);
    }
}
