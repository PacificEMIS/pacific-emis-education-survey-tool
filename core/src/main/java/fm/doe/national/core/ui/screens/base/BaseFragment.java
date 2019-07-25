package fm.doe.national.core.ui.screens.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpAppCompatFragment;

import butterknife.ButterKnife;
import fm.doe.national.core.data.exceptions.GmsUserRecoverableException;
import fm.doe.national.core.ui.views.InputDialog;

public class BaseFragment extends MvpAppCompatFragment implements BaseView {

    @Nullable
    private Dialog masterPasswordDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (masterPasswordDialog != null) {
            masterPasswordDialog.dismiss();
        }
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

    @Nullable
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void promptMasterPassword(Text title) {
        if (getPresenter() == null) {
            throw new IllegalStateException("Fragment must override getPresenter() to prompt master password");
        }

        masterPasswordDialog = InputDialog.create(getContext(), title, null)
                .setListener(getPresenter()::onMasterPasswordSubmit);
        masterPasswordDialog.show();
    }

    @Override
    public void handleGmsRecoverableException(GmsUserRecoverableException throwable) {
        ((BaseActivity) getActivity()).handleGmsRecoverableException(throwable);
    }
}
