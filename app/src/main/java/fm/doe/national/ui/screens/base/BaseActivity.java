package fm.doe.national.ui.screens.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.omega_r.libs.omegatypes.Text;

import java.io.Serializable;

import butterknife.ButterKnife;
import fm.doe.national.R;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
    }

    protected abstract @LayoutRes
    int getContentView();

    protected void initToolbar() {
        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null && getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
            }
            toolbar.setNavigationOnClickListener(v -> onHomePressed());
        }
    }

    protected void onHomePressed() {
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
        Toast.makeText(this, text.getString(getResources()), Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard() {
        hideKeyboard(getWindow().getDecorView());
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    public void showWarning(Text title, Text message) {
        new AlertDialog.Builder(this)
                .setTitle(title.getString(getResources()))
                .setMessage(message.getString(getResources()))
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

}
