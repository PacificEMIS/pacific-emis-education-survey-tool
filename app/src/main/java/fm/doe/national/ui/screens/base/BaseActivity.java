package fm.doe.national.ui.screens.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.omega_r.libs.omegatypes.Text;

import java.io.Serializable;

import butterknife.ButterKnife;
import fm.doe.national.R;

@SuppressLint("Registered")
public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
    }

    protected abstract @LayoutRes int getContentView();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    protected void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(!isTaskRoot());
            }
        }
    }

    @Override
    public void showToast(Text text) {
        Toast.makeText(this, text.getString(getResources()), Toast.LENGTH_SHORT).show();
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
    protected <T extends Serializable> T getSerializableArgument(String extraName) {
        try {
            return (T) getIntent().getSerializableExtra(extraName);
        } catch (NullPointerException | ClassCastException ex) {
            throw new RuntimeException("Unable to obtain serializable argument");
        }
    }
}
