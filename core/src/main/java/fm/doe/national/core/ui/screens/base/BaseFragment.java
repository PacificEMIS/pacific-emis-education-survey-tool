package fm.doe.national.core.ui.screens.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

    private static final int REQUEST_EXTERNAL_DOCUMENT = 100;

    @Nullable
    private Dialog masterPasswordDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_DOCUMENT:
                if (resultCode == Activity.RESULT_OK) {
                    BasePresenter presenter = getPresenter();
                    if (data != null && presenter != null) {
                        presenter.onExternalDocumentPicked(getActivity().getContentResolver(), data.getData());
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void openExternalDocumentsPicker(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(mimeType);
        startActivityForResult(intent, REQUEST_EXTERNAL_DOCUMENT);
    }
}
