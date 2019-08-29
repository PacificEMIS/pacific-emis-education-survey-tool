package fm.doe.national.core.ui.screens.base;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.data.exceptions.GmsUserRecoverableException;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseView extends MvpView {

    void showToast(Text text);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(Text title, Text message);

    void showWaiting();

    void hideWaiting();

    void promptMasterPassword(Text title);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void handleGmsRecoverableException(GmsUserRecoverableException throwable);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openExternalDocumentsPicker(String mimeType);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showPrompt(Text title, Text message, Runnable onPositivePressed);
}
