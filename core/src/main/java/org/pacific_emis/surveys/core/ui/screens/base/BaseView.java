package org.pacific_emis.surveys.core.ui.screens.base;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.core.data.exceptions.GmsUserRecoverableException;


public interface BaseView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showToast(Text text);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(Text title, Text message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showWaiting();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideWaiting();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void promptMasterPassword(Text title);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void handleGmsRecoverableException(GmsUserRecoverableException throwable);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openExternalDocumentsPicker(String mimeType);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showPrompt(Text title, Text message, Runnable onPositivePressed);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

}
