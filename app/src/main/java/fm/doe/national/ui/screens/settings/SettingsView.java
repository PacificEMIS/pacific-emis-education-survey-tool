package fm.doe.national.ui.screens.settings;

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

}
