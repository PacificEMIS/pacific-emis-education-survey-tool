package fm.doe.national.core.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.R;

public class InputFieldLayout extends FrameLayout implements View.OnClickListener, TextWatcher {

    private static final boolean DEFAULT_DONE_BUTTON_VISIBLE = true;
    private static final int NULL_MAX_LENGTH = -1;

    private EditText editText;
    private ImageButton doneButton;

    private String startingText;

    @Nullable
    private OnDonePressedListener listener;

    public InputFieldLayout(@NonNull Context context) {
        this(context, null);
    }

    public InputFieldLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputFieldLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public InputFieldLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_input_field, this);
        bindViews();
        applyAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void bindViews() {
        editText = findViewById(R.id.textinputedittext);
        editText.addTextChangedListener(this);
        doneButton = findViewById(R.id.imagebutton_done);
        doneButton.setOnClickListener(this);
    }

    private void applyAttributes(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputFieldLayout, defStyleAttr, defStyleRes);
        setDoneButtonVisible(a.getBoolean(R.styleable.InputFieldLayout_doneVisible, DEFAULT_DONE_BUTTON_VISIBLE));
        int inputType = a.getInt(R.styleable.InputFieldLayout_android_inputType, EditorInfo.TYPE_NULL);
        if (inputType != EditorInfo.TYPE_NULL) {
            setInputType(inputType);
        }
        int maxLength = a.getInt(R.styleable.InputFieldLayout_android_maxLength, NULL_MAX_LENGTH);
        if (maxLength != NULL_MAX_LENGTH) {
            editText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(maxLength)});
        }
        a.recycle();
    }

    public void setDoneButtonVisible(boolean isVisible) {
        doneButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setInputType(int inputType) {
        editText.setInputType(inputType);
    }

    public void setOnDonePressedListener(OnDonePressedListener listener) {
        this.listener = listener;
    }

    public void setStartingText(String text) {
        startingText = text;
        editText.setText(startingText);
        setDoneButtonVisible(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imagebutton_done) {
            final String enteredText = editText.getText().toString();
            startingText = enteredText.isEmpty() ? null : enteredText;
            setDoneButtonVisible(false);

            if (listener != null) {
                listener.onDonePressed(this, startingText);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        setDoneButtonVisible(!editText.getText().toString().equals(startingText));
    }

    public interface OnDonePressedListener {
        void onDonePressed(View view, @Nullable String content);
    }
}
