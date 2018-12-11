package fm.doe.national.ui.screens.settings;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends BaseView {
    void showAccountConnections(List<CloudAccountData> viewDataList);
    void hideConnectViews(List<CloudType> types);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void pickPhotoFromGallery();

    void setLogo(String path);
    void setLogoName(String logoName);
}
