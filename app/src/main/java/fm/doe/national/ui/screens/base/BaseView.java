package fm.doe.national.ui.screens.base;

import com.arellomobile.mvp.MvpView;
import com.omega_r.libs.omegatypes.Text;

public interface BaseView extends MvpView {

    void showToast(Text text);
    void showMessage(Text title, Text message);
    void showWaiting();
    void hideWaiting();
}
