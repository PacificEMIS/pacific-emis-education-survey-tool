package fm.doe.national.ui.screens.settings;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.settings.items.Item;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void pickPhotoFromGallery();

    void setOptions(List<Item> options);


    @StateStrategyType(OneExecutionStateStrategy.class)
    void showInputDialog(@Nullable Text title, @Nullable Text existingText, InputListener listener);

    interface InputListener {
        void onInput(String string);
    }

}
