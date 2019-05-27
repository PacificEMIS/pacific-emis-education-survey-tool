package fm.doe.national.core.ui.screens.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Date;

import butterknife.ButterKnife;
import fm.doe.national.core.R;
import fm.doe.national.core.ui.views.ProgressDialogFragment;
import fm.doe.national.core.utils.DateUtils;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    private static final String TAG_PROGRESS_DIALOG = "TAG_PROGRESS_DIALOG";

    @Nullable
    protected ImageView clockIconImageView;

    @Nullable
    protected TextView titleTextView;

    @Nullable
    protected TextView yearTextView;

    @Nullable
    protected Toolbar toolbar;

    @Nullable
    private ProgressDialogFragment progressDialog = null;
    private int progressDialogsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        bindToolbarViews();
        initToolbar();
    }

    private void bindToolbarViews() {
        toolbar = findViewById(R.id.toolbar);
        yearTextView = findViewById(R.id.textview_toolbar_year);
        titleTextView = findViewById(R.id.textview_toolbar_title);
        clockIconImageView = findViewById(R.id.imageview_toolbar_clock);
    }

    @LayoutRes
    protected abstract int getContentView();

    protected void initToolbar() {
        if (toolbar != null && getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP |
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

    protected void setToolbarMode(ToolbarDisplaying mode) {
        if (titleTextView == null) return;
        switch (mode) {
            case PRIMARY:
                titleTextView.setActivated(false);
                break;
            case SECONDARY:
                titleTextView.setActivated(true);
                break;
        }
    }

    public void setToolbarDate(Date date) {
        if (clockIconImageView == null || yearTextView == null) return;
        yearTextView.setText(DateUtils.formatMonthYear(date));
        clockIconImageView.setVisibility(View.VISIBLE);
        yearTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(int resId) {
        if (titleTextView == null) return;
        titleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (titleTextView == null) return;
        titleTextView.setText(title);
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

    protected enum ToolbarDisplaying {
        PRIMARY, SECONDARY
    }
}