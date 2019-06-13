package fm.doe.national.core.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;
import fm.doe.national.core.ui.screens.base.BaseDialogFragment;
import fm.doe.national.core.utils.Constants;

public class InputDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_TEXT = "ARG_TEXT";

    private TextView titleTextView;
    private EditText editText;
    private Button submitButton;
    private Button cancelButton;

    @Nullable
    private Listener listener;

    public static InputDialogFragment create(@Nullable Text title, @Nullable Text existingText) {
        InputDialogFragment dialog = new InputDialogFragment();
        Bundle args = new Bundle();

        if (title != null) {
            args.putSerializable(ARG_TITLE, title);
        }

        if (existingText != null) {
            args.putSerializable(ARG_TEXT, existingText);
        }

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupLayout();
    }

    private void setupLayout() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        parseArgs();
        view.setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void bindViews(View view) {
        titleTextView = view.findViewById(R.id.textview_title);
        editText = view.findViewById(R.id.textinputedittext);
        submitButton = view.findViewById(R.id.button_confirm);
        cancelButton = view.findViewById(R.id.button_cancel);
    }

    private void parseArgs() {
        Bundle bundle = getArguments();

        if (bundle == null) {
            throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);
        }

        Text title = (Text) bundle.getSerializable(ARG_TITLE);
        Text text = (Text) bundle.getSerializable(ARG_TEXT);

        if (title != null) {
            title.applyTo(titleTextView);
        }

        if (text != null) {
            editText.setText(text.getString(getContext()));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_confirm) {
            if (listener != null) listener.onInputDialogSubmit(editText.getText().toString());
            dismiss();
        } else if (id == R.id.button_cancel) {
            dismiss();
        }
    }

    public interface Listener {
        void onInputDialogSubmit(String input);
    }
}
