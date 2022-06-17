package org.pacific_emis.surveys.core.ui.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.R;
import org.pacific_emis.surveys.core.utils.DateUtils;

import java.util.Calendar;

public class InputDialog extends Dialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @Nullable
    private final Text title;

    @Nullable
    private final Text existingText;

    private TextView titleTextView;
    private EditText editText;
    private Button submitButton;
    private Button cancelButton;

    private Calendar calendar = Calendar.getInstance();
    private Boolean clickable = false;

    @Nullable
    private Listener listener;

    public static InputDialog create(Context context, @Nullable Text title, @Nullable Text existingText) {
        return new InputDialog(context, title, existingText, false);
    }

    public static InputDialog create(Context context, @Nullable Text title, @Nullable Text existingText, Boolean clickable) {
        return new InputDialog(context, title, existingText, clickable);
    }

    private InputDialog(@NonNull Context context, @Nullable Text title, @Nullable Text existingText, Boolean clickable) {
        super(context);
        this.title = title;
        this.existingText = existingText;
        if (clickable) this.clickable = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        bindViews();
        setCancelable(true);
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        if (title != null) {
            title.applyTo(titleTextView);
        }

        if (existingText != null) {
            editText.setText(existingText.getString(getContext()));
        }

        if (clickable) {
            editText.setCursorVisible(false);
            editText.setOnClickListener(this);
        } else {
            editText.setOnClickListener(null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setupLayout();
    }

    private void setupLayout() {
        Window window = getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public InputDialog setListener(@Nullable Listener listener) {
        this.listener = listener;
        return this;
    }

    private void bindViews() {
        titleTextView = findViewById(R.id.textview_title);
        editText = findViewById(R.id.textinputedittext);
        submitButton = findViewById(R.id.button_confirm);
        cancelButton = findViewById(R.id.button_cancel);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_confirm) {
            if (listener != null) listener.onInputDialogSubmit(editText.getText().toString());
            dismiss();
        } else if (id == R.id.button_cancel) {
            dismiss();
        } else if (id == R.id.textinputedittext) {
            new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        editText.setText(DateUtils.formatDateTag(calendar.getTime()));
    }

    public interface Listener {
        void onInputDialogSubmit(String input);
    }
}
