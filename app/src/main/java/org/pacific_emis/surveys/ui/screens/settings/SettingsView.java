package org.pacific_emis.surveys.ui.screens.settings;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.OperatingMode;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;
import org.pacific_emis.surveys.ui.screens.settings.items.Item;

public interface SettingsView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setItems(List<Item> options);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showInputDialog(@Nullable Text title, @Nullable Text existingText, InputListener listener);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showRegionSelector(RegionListener listener);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showOperatingModeSelector(OperatingModeListener listener);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToChangePassword();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToChangeLogo();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToTemplates();

    interface InputListener {
        void onInput(String string);
    }

    interface RegionListener {
        void onRegionSelected(AppRegion region);
    }

    interface OperatingModeListener {
        void onOperatingModeSelected(OperatingMode opMode);
    }

}
