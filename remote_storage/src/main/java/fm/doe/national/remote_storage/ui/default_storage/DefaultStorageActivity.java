package fm.doe.national.remote_storage.ui.default_storage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;

public class DefaultStorageActivity extends BaseActivity implements DefaultStorageView {

    private final static int REQUEST_CODE_OPEN_DOC = 987;

    @InjectPresenter
    DefaultStoragePresenter presenter;

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, DefaultStorageActivity.class);
    }

    @ProvidePresenter
    DefaultStoragePresenter providePresenter() {
        return new DefaultStoragePresenter(RemoteStorageComponentInjector.getComponent(getApplication()));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_default_storage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_OPEN_DOC && resultCode == Activity.RESULT_OK && data != null) {
            presenter.onUriReceived(getContentResolver(), data.getData());
        } else {
            presenter.onNothingReceived();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showPicker() {
        startActivityForResult(
                new Intent(Intent.ACTION_OPEN_DOCUMENT)
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*"),
                REQUEST_CODE_OPEN_DOC);
    }

    @Override
    public void close() {
        finish();
    }
}
