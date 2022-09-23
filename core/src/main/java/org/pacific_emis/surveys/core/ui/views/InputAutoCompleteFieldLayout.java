package org.pacific_emis.surveys.core.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.pacific_emis.surveys.core.R;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.utils.DebounceTextChangedWatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InputAutoCompleteFieldLayout extends FrameLayout implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final boolean DEFAULT_DONE_BUTTON_VISIBLE = true;
    private static final int NULL_MAX_LENGTH = -1;
    private static final long DELAY_CHANGED_MILLIS = 100;

    private AutoCompleteTextView textView;
    private ArrayAdapter<?> adapter;
    private ImageButton doneButton;

    private String startingText;

    @Nullable
    private OnDonePressedListener listener;

    public InputAutoCompleteFieldLayout(@NonNull Context context) {
        this(context, null);
    }

    public InputAutoCompleteFieldLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputAutoCompleteFieldLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public InputAutoCompleteFieldLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_input_auto_comlete_field, this);
        bindViews();
        applyAttributes(context, attrs, defStyleAttr, defStyleRes);
        DebounceTextChangedWatcher.setup(
                textView,
                DELAY_CHANGED_MILLIS,
                newText -> {
                    if (newText.equals("")) {
                        if (startingText == null) setDoneButtonVisible(false);
                    } else {
                        setDoneButtonVisible(!newText.equals(startingText));
                    }
                }
        );
    }

    private void bindViews() {
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());

        textView = findViewById(R.id.input_auto_complete_text);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(this);

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
            textView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(maxLength)});
        }
        a.recycle();
    }

    public void setDoneButtonVisible(boolean isVisible) {
        doneButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setInputType(int inputType) {
        textView.setInputType(inputType);
    }

    public void setOnDonePressedListener(OnDonePressedListener listener) {
        this.listener = listener;
    }

    public void setStartingText(String text) {
        startingText = text;
        textView.setText(startingText);
        setDoneButtonVisible(false);
    }

    public void setContentList(@NotNull List<?> content) {
        setAdapterContent(adapter, content);
    }

    private void setAdapterContent(ArrayAdapter<?> adapter, List<?> newContent) {
        adapter.clear();
        adapter.addAll((Collection) newContent);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imagebutton_done) {
            final String enteredText = textView.getText().toString();
            startingText = enteredText.isEmpty() ? null : enteredText;
            setDoneButtonVisible(false);

            if (listener != null) {
                listener.onDonePressed(this, startingText);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setDoneButtonVisible(true);
    }

    public interface OnDonePressedListener {
        void onDonePressed(View view, @Nullable Object content);
    }

}