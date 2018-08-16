package fm.doe.national.ui.screens.search.base;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.ui.screens.base.BaseView;

/**
 * Created by Alexander Chibirev on 8/16/2018.
 */

public interface BaseSearchView extends BaseView {

    @StateStrategyType(SkipStrategy.class)
    void openSearch();
    @StateStrategyType(SkipStrategy.class)
    void closeSearch();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showNotFoundObject(Text message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideNotFoundObject();


}
