package fm.doe.national.ui.screens.splash;

import android.support.annotation.NonNull;
import android.support.transition.Transition;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.ui.screens.base.BaseView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startAnimate(@NonNull Transition transition);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSchoolAccreditationScreen();

    void setSchools(List<School> schools);

}
