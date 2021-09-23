package org.pacific_emis.surveys.accreditation.ui.observation_info;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;

public class TimeOut implements TextWatcher {

    @Nullable
    private final Runnable run;
    private final long delay;

    final Handler handler = new Handler(Looper.getMainLooper());

    TimeOut(@Nullable Runnable run, long delay) {
        this.run = run;
        this.delay = delay;
    }

    private void timeout(Runnable run, long delay) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(run, delay);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        timeout(run, delay);
    }

}
