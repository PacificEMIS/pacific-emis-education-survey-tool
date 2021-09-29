package org.pacific_emis.surveys.core.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class DebounceTextChangedWatcher implements TextWatcher, View.OnFocusChangeListener {

    @NonNull
    private final OnDebounceTextChangedListener listener;
    private final long delayInMillis;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable changedRunnable = new Runnable() {
        @Override
        public void run() {
            listener.onDebounceTextChanged(currentText);
        }
    };

    private String currentText;

    private DebounceTextChangedWatcher(long delayInMillis, @NonNull OnDebounceTextChangedListener listener) {
        this.listener = listener;
        this.delayInMillis = delayInMillis;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        currentText = charSequence.toString();
        handler.removeCallbacks(changedRunnable);
        handler.postDelayed(changedRunnable, delayInMillis);
    }

    @Override
    public void afterTextChanged(Editable s) {
        //nothing
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && handler.hasCallbacks(changedRunnable)) {
            handler.removeCallbacks(changedRunnable);
            listener.onDebounceTextChanged(currentText);
        }
    }

    public static void setup(EditText editText, long delayInMillis, @NonNull OnDebounceTextChangedListener listener) {
        DebounceTextChangedWatcher debounceTextWatcher = new DebounceTextChangedWatcher(delayInMillis, listener);
        editText.addTextChangedListener(debounceTextWatcher);
        editText.setOnFocusChangeListener(debounceTextWatcher);
    }

    public interface OnDebounceTextChangedListener {
        void onDebounceTextChanged(String newText);
    }

}
