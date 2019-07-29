package fm.doe.national.core.ui.screens.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpAppCompatActivity;

import java.io.Serializable;

import butterknife.ButterKnife;
import fm.doe.national.core.R;
import fm.doe.national.core.data.exceptions.GmsUserRecoverableException;
import fm.doe.national.core.ui.views.InputDialog;
import fm.doe.national.core.ui.views.ProgressDialogFragment;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    private static final String TAG_PROGRESS_DIALOG = "TAG_PROGRESS_DIALOG";
    private static final int REQUEST_EXTERNAL_DOCUMENT = 100;

    @Nullable
    protected Toolbar toolbar;

    @Nullable
    private ProgressDialogFragment progressDialog = null;
    private int progressDialogsCount = 0;

    @Nullable
    private Dialog masterPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        bindToolbarViews();
        initToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_DOCUMENT:
                if (resultCode == Activity.RESULT_OK) {
                    BasePresenter presenter = getPresenter();
                    if (data != null && presenter != null) {
                        presenter.onExternalDocumentPicked(getContentResolver(), data.getData());
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (masterPasswordDialog != null) {
            masterPasswordDialog.dismiss();
        }
    }

    private void bindToolbarViews() {
        toolbar = findViewById(R.id.toolbar);
    }

    @LayoutRes
    protected abstract int getContentView();

    protected void initToolbar() {
        if (toolbar != null && getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            ActionBar supportActionBar = getSupportActionBar();

            if (supportActionBar != null) {
                supportActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP |
                        ActionBar.DISPLAY_SHOW_HOME |
                        ActionBar.DISPLAY_SHOW_CUSTOM);
            }

            toolbar.setNavigationOnClickListener(v -> onHomePressed());
        }
    }

    public void onHomePressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (upIntent != null) {
            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                NavUtils.navigateUpTo(this, upIntent);
            }
        } else {
            finish();
        }
    }

    @Override
    public void showToast(Text text) {
        Toast.makeText(this, text.getString(this), Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard() {
        hideKeyboard(getWindow().getDecorView());
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    public void showMessage(Text title, Text message) {
        new AlertDialog.Builder(this)
                .setTitle(title.getString(this))
                .setMessage(message.getString(this))
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    @SuppressWarnings("unchecked")
    protected <T extends Serializable> T getSerializableExtra(String extraName) {
        try {
            return (T) getIntent().getSerializableExtra(extraName);
        } catch (NullPointerException | ClassCastException ex) {
            throw new RuntimeException("Unable to obtain serializable argument");
        }
    }

    @Override
    public void showWaiting() {
        progressDialogsCount++;
        if (progressDialogsCount == 1) {
            progressDialog = createProgressDialog(Text.empty());
        }
    }

    @Override
    public void hideWaiting() {
        if (progressDialogsCount == 0) {
            return;
        }

        if (progressDialogsCount == 1 && progressDialog != null) {
            progressDialog.dismiss();
        }

        progressDialogsCount--;
    }

    @NonNull
    private ProgressDialogFragment createProgressDialog(Text text) {
        ProgressDialogFragment dialog = ProgressDialogFragment.create();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), TAG_PROGRESS_DIALOG);
        return dialog;
    }

    protected void replaceFragment(@IdRes int containerId, BaseFragment fragment) {
        if (getFragmentManager() == null) return;
        getSupportFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Nullable
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void promptMasterPassword(Text title) {
        if (getPresenter() == null) {
            throw new IllegalStateException("Activity must override getPresenter() to prompt master password");
        }

        masterPasswordDialog = InputDialog.create(this, title, null)
                .setListener(getPresenter()::onMasterPasswordSubmit);
        masterPasswordDialog.show();
    }

    @Override
    public void handleGmsRecoverableException(GmsUserRecoverableException ex) {
        startActivity(ex.getIntent());
    }

    @Override
    public void openExternalDocumentsPicker(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(mimeType);
        startActivityForResult(intent, REQUEST_EXTERNAL_DOCUMENT);
    }
}
