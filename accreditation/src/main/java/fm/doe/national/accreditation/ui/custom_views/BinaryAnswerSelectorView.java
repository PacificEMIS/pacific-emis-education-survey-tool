package fm.doe.national.accreditation.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import fm.doe.national.accreditation.R;

public class BinaryAnswerSelectorView extends LinearLayout implements View.OnClickListener {

    private RadioButton positiveRadioButton;
    private RadioButton negativeRadioButton;
    private View positiveView;
    private View negativeView;


    private State state = State.NEUTRAL;

    @Nullable
    private StateChangedListener listener;

    public BinaryAnswerSelectorView(Context context) {
        this(context, null);
    }

    public BinaryAnswerSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BinaryAnswerSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BinaryAnswerSelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_binary_answer_selector, this);
        bindViews();
        positiveView.setOnClickListener(this);
        negativeView.setOnClickListener(this);
    }

    private void bindViews() {
        positiveRadioButton = findViewById(R.id.radiobutton_positive);
        negativeRadioButton = findViewById(R.id.radiobutton_negative);
        positiveView = findViewById(R.id.layout_positive);
        negativeView = findViewById(R.id.layout_negative);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.layout_positive) {
            setState(state == State.POSITIVE ? State.NEUTRAL : State.POSITIVE);
        } else if (id == R.id.layout_negative) {
            setState(state == State.NEGATIVE ? State.NEUTRAL : State.NEGATIVE);
        }
    }

    public State getState() {
        return state;
    }

    public void setStateNotNotifying(State state) {
        this.state = state;
        switch (state) {
            case POSITIVE:
                becomePositive();
                break;
            case NEGATIVE:
                becomeNegative();
                break;
            case NEUTRAL:
                becomeNeutral();
                break;
        }
    }

    public void setState(State state) {
        setStateNotNotifying(state);
        if (listener != null) listener.onStateChanged(this, state);
    }

    @Nullable
    public StateChangedListener getListener() {
        return listener;
    }

    public void setListener(@Nullable StateChangedListener listener) {
        this.listener = listener;
    }

    private void becomePositive() {
        positiveRadioButton.setChecked(true);
        negativeRadioButton.setChecked(false);

        positiveView.setActivated(true);
        negativeView.setActivated(false);
    }

    private void becomeNegative() {
        positiveRadioButton.setChecked(false);
        negativeRadioButton.setChecked(true);

        positiveView.setActivated(false);
        negativeView.setActivated(true);
    }

    private void becomeNeutral() {
        positiveRadioButton.setChecked(false);
        negativeRadioButton.setChecked(false);

        positiveView.setActivated(false);
        negativeView.setActivated(false);
    }

    public enum State {
        NEUTRAL, POSITIVE, NEGATIVE
    }

    public interface StateChangedListener {
        void onStateChanged(BinaryAnswerSelectorView view, State state);
    }

}
