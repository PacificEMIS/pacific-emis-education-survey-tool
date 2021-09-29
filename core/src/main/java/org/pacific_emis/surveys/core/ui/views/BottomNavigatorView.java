package org.pacific_emis.surveys.core.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.R;

public class BottomNavigatorView extends RelativeLayout implements View.OnClickListener {

    private TextView hintTextView;
    private TextView prevButton;
    private TextView nextButton;
    private TextView uploadTextView;
    private ImageView uploadImageView;
    private LinearLayout uploadLayout;

    @Nullable
    private Listener listener;

    public BottomNavigatorView(Context context) {
        this(context, null);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_bottom_navigator, this);
        bindViews();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigatorView, defStyleAttr, defStyleRes);
        setPrevText(a.getString(R.styleable.BottomNavigatorView_prevText));
        setNextText(a.getString(R.styleable.BottomNavigatorView_nextText));
        setHintText(a.getString(R.styleable.BottomNavigatorView_hintText));
        a.recycle();

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    private void bindViews() {
        hintTextView = findViewById(R.id.textview_complete_hint);
        prevButton = findViewById(R.id.button_prev);
        nextButton = findViewById(R.id.button_next);
        uploadTextView = findViewById(R.id.textview_upload_to_cloud);
        uploadImageView = findViewById(R.id.imageview_upload_to_cloud);
        uploadLayout = findViewById(R.id.layout_upload);
    }

    public void setPrevText(@Nullable String text) {
        updateView(prevButton, text);
    }

    public void setPrevButtonVisible(boolean isVisible) {
        prevButton.setVisibility(isVisible ? VISIBLE : GONE);
        setCenter(isVisible);
    }

    public void setNextButtonVisible(boolean isVisible) {
        nextButton.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setHintTextVisible(boolean isVisible) {
        hintTextView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    public void setUploadStateVisible(boolean isVisible) {
        uploadTextView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
        uploadImageView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    public void setPrevButtonEnabled(boolean isEnabled) {
        prevButton.setEnabled(isEnabled);
    }

    public void setNextButtonEnabled(boolean isEnabled) {
        nextButton.setEnabled(isEnabled);
    }

    public void setNextText(@Nullable String text) {
        updateView(nextButton, text);
    }

    public void setHintText(@Nullable String text) {
        updateView(hintTextView, text, INVISIBLE);
    }

    private void updateView(TextView view, @Nullable String text) {
        updateView(view, text, GONE);
    }

    private void updateView(TextView view, @Nullable String text, int disappearVisibility) {
        view.setText(text);

        if (text == null) {
            view.setVisibility(disappearVisibility);
        }
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        int id = v.getId();

        if (id == R.id.button_prev) {
            listener.onPrevPressed();
        } else if (id == R.id.button_next) {
            listener.onNextPressed();
        }
    }

    public void setUploadInProgress() {
        uploadTextView.setText(R.string.label_synced_in_progress);
        uploadImageView.setImageResource(R.drawable.ic_in_progress_synced);
    }

    public void setUploadSuccessfully() {
        uploadTextView.setText(R.string.label_synced_successfully);
        uploadImageView.setImageResource(R.drawable.ic_successfully_synced);
    }

    public void setNotUpload() {
        uploadTextView.setText(R.string.label_has_not_synced);
        uploadImageView.setImageResource(R.drawable.ic_not_synced);
    }

    private void setCenter(boolean center) {
        if(!center) {
            uploadLayout.setGravity(Gravity.LEFT);
        } else {
            uploadLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    public interface Listener {
        void onPrevPressed();
        void onNextPressed();
    }
}
