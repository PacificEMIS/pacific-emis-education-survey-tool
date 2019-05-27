package fm.doe.national.core.ui.screens.base;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpView;

public interface BaseView extends MvpView {

    void showToast(Text text);
    void showMessage(Text title, Text message);
    void showWaiting();
    void hideWaiting();
}
