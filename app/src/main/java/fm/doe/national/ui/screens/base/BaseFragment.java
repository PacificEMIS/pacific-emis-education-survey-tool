package fm.doe.national.ui.screens.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpAppCompatFragment;

import butterknife.ButterKnife;

public class BaseFragment extends MvpAppCompatFragment implements BaseView {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(Text text) {
        ((BaseActivity)getActivity()).showToast(text);
    }

    @Override
    public void showMessage(Text title, Text message) {
        ((BaseActivity)getActivity()).showMessage(title, message);
    }

    @Override
    public void showWaiting() {
        ((BaseActivity)getActivity()).showWaiting();
    }

    @Override
    public void hideWaiting() {
        ((BaseActivity)getActivity()).hideWaiting();
    }

}
