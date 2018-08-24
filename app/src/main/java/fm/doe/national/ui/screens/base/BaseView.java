package fm.doe.national.ui.screens.base;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.omega_r.libs.omegatypes.Text;

public interface BaseView extends MvpView {

    void showToast(Text text);
    void showWarning(@StringRes int title, @StringRes int message);

}
