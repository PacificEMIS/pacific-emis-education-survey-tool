package org.pacific_emis.surveys.ui.screens.menu;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.image.Image;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.offline_sync.ui.base.BaseBluetoothView;

public interface MainMenuView extends BaseBluetoothView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setIcon(Image icon);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setTitle(Text title);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurveys();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSettings();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToLicense();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMergeProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setAccountName(@Nullable String accountName);

}
