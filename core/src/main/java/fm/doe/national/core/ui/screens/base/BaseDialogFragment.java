package fm.doe.national.core.ui.screens.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpAppCompatDialogFragment;

import butterknife.ButterKnife;
import fm.doe.national.core.data.exceptions.GmsUserRecoverableException;

public class BaseDialogFragment extends MvpAppCompatDialogFragment implements BaseView {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(Text text) {
        // nothing
    }

    @Override
    public void showMessage(Text title, Text message) {
        // nothing
    }

    @Override
    public void showWaiting() {
        // nothing
    }

    @Override
    public void hideWaiting() {
        // nothing
    }

    @Override
    public void promptMasterPassword(Text title) {
        // nothing
    }

    @Override
    public void handleGmsRecoverableException(GmsUserRecoverableException throwable) {
        ((BaseActivity) getActivity()).handleGmsRecoverableException(throwable);
    }
}
