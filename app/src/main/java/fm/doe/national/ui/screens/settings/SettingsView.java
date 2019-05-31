package fm.doe.national.ui.screens.settings;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.cloud.model.CloudAccountData;
import fm.doe.national.cloud.model.CloudType;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends BaseView {
    void showAccountConnections(List<CloudAccountData> viewDataList);

    void hideConnectViews(List<CloudType> types);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void pickPhotoFromGallery();

    void setLogo(String path);

    void setLogoName(String logoName);

    void setAppContext(AppRegion appRegion);
}
