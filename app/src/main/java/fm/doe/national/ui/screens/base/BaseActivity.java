package fm.doe.national.ui.screens.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.omega_r.libs.omegatypes.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

@SuppressLint("Registered")
public class BaseActivity extends MvpAppCompatActivity implements BaseView {

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    protected void initToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView();
    }

    protected void showBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView();
    }

    protected void afterSetContentView() {
        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    public void showToast(Text text) {
        Toast.makeText(this, text.getString(getResources()), Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        hideKeyboard(getWindow().getDecorView());
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
