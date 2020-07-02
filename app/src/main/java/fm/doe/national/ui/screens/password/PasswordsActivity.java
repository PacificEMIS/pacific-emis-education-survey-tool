package fm.doe.national.ui.screens.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.core.utils.TextWatcherAdapter;
import fm.doe.national.ui.screens.region.ChooseRegionActivity;

public class PasswordsActivity extends BaseActivity implements PasswordView, BottomNavigatorView.Listener {

    private static final String EXTRA_IS_SETTINGS = "EXTRA_IS_SETTINGS";

    @InjectPresenter
    PasswordPresenter presenter;

    private View notMatchView;
    private View passwordShortView;
    private boolean isInSettingsContext;

    private final TextWatcher newPassTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            presenter.onNewPasswordInput(s.toString());
        }
    };

    private final TextWatcher confirmPassTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            presenter.onConfirmPasswordInput(s.toString());
        }
    };

    @NonNull
    public static Intent createIntent(Context context, boolean isSettings) {
        return new Intent(context, PasswordsActivity.class).putExtra(EXTRA_IS_SETTINGS, isSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isInSettingsContext = getIntent().getBooleanExtra(EXTRA_IS_SETTINGS, false);
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_passwords);
        bindViews();
    }

    private void bindViews() {
        setTextWatcher(R.id.textinputedittext_new_pass, newPassTextWatcher);
        setTextWatcher(R.id.textinputedittext_confirm_pass, confirmPassTextWatcher);
        notMatchView = findViewById(R.id.textview_incorrect);
        passwordShortView = findViewById(R.id.textview_short_password);

        BottomNavigatorView navigatorView = findViewById(R.id.bottomnavigatorview);

        if (navigatorView != null) {
            navigatorView.setListener(this);
        }

        Button confirmButton = findViewById(R.id.button_confirm);

        if (confirmButton != null) {
            confirmButton.setOnClickListener(v -> presenter.onConfirmPressed());
        }
    }

    private void setTextWatcher(@IdRes int idRes, TextWatcher watcher) {
        ((EditText) findViewById(idRes)).addTextChangedListener(watcher);
    }

    @Override
    protected int getContentView() {
        return isInSettingsContext ? R.layout.activity_password_settings : R.layout.activity_password_wizard;
    }

    @Override
    public void setPasswordsNotMatchVisible(boolean visible) {
        notMatchView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPasswordTooShortVisible(boolean visible) {
        passwordShortView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToRegion() {
        if (isInSettingsContext) {
            finish();
        } else {
            startActivity(ChooseRegionActivity.createIntent(this));
        }
    }

    @Override
    public void onPrevPressed() {
        // nothing
    }

    @Override
    public void onNextPressed() {
        presenter.onConfirmPressed();
    }
}
