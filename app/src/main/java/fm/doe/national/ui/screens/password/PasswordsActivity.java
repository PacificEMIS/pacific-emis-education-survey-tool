package fm.doe.national.ui.screens.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.core.utils.TextWatcherAdapter;
import fm.doe.national.ui.screens.authentication.AuthenticationActivity;

public class PasswordsActivity extends BaseActivity implements PasswordView, BottomNavigatorView.Listener {

    private static final String EXTRA_IS_SETTINGS = "EXTRA_IS_SETTINGS";

    @InjectPresenter
    PasswordPresenter presenter;

    private View notMatchView;
    private boolean isIsSettingsContext;

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
        isIsSettingsContext = getIntent().getBooleanExtra(EXTRA_IS_SETTINGS, false);
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_passwords);
        bindViews();
    }

    private void bindViews() {
        ((EditText) findViewById(R.id.textinputedittext_new_pass)).addTextChangedListener(newPassTextWatcher);
        ((EditText) findViewById(R.id.textinputedittext_confirm_pass)).addTextChangedListener(confirmPassTextWatcher);
        notMatchView = findViewById(R.id.textview_incorrect);

        BottomNavigatorView bnv = findViewById(R.id.bottomnavigatorview);

        if (bnv != null) {
            bnv.setListener(this);
        }

        FloatingActionButton fab = findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(v -> presenter.onConfirmPressed());
        }
    }

    @Override
    protected int getContentView() {
        return isIsSettingsContext ? R.layout.activity_password_settings : R.layout.activity_password_wizard;
    }

    @Override
    public void setPasswordsNotMatchVisible(boolean visible) {
        notMatchView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToSignIn() {
        if (isIsSettingsContext) {
            finish();
        } else {
            startActivity(AuthenticationActivity.createIntent(this));
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
