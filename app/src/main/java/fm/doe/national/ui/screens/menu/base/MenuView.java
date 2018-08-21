package fm.doe.national.ui.screens.menu.base;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MenuView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSchoolAccreditationScreen();

    void setTests(List<School> schools);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void pickPhotoFromGallery();

}
