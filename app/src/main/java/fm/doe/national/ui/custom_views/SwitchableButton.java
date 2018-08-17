package fm.doe.national.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class SwitchableButton extends FrameLayout {

    @BindView(R.id.button_positive)
    Button positiveButton;

    @BindView(R.id.button_negative)
    Button negativeButton;

    private State state;
    private StateChangedListener listener;

    public SwitchableButton(Context context) {
        this(context, null);
    }

    public SwitchableButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwitchableButton(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        inflate(getContext(), R.layout.switch_answer, this);
        ButterKnife.bind(this);

        state = State.NEUTRAL;

        positiveButton.setOnClickListener((View v) -> setState(state == State.POSITIVE ? State.NEUTRAL : State.POSITIVE));
        negativeButton.setOnClickListener((View v) -> setState(state == State.NEGATIVE ? State.NEUTRAL : State.NEGATIVE));
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.POSITIVE) {
            becomePositive();
        } else if (state == State.NEGATIVE) {
            becomeNegative();
        } else {
            becomeNeutral();
        }

        if (listener != null) listener.onStateChanged(this, state);
    }

    public StateChangedListener getListener() {
        return listener;
    }

    public void removeListener() {
        listener = null;
    }

    public void setListener(StateChangedListener listener) {
        this.listener = listener;
    }

    private void becomePositive() {
        positiveButton.setTextColor(getResources().getColor(R.color.white));
        negativeButton.setTextColor(getResources().getColor(R.color.black));

        positiveButton.setBackgroundResource(R.drawable.ic_yes_no_left_active);
        negativeButton.setBackgroundResource(R.drawable.ic_yes_no_right);
    }

    private void becomeNegative() {
        positiveButton.setTextColor(getResources().getColor(R.color.black));
        negativeButton.setTextColor(getResources().getColor(R.color.white));

        positiveButton.setBackgroundResource(R.drawable.ic_yes_no_left);
        negativeButton.setBackgroundResource(R.drawable.ic_yes_no_right_active);
    }

    private void becomeNeutral() {
        positiveButton.setTextColor(getResources().getColor(R.color.black));
        negativeButton.setTextColor(getResources().getColor(R.color.black));

        positiveButton.setBackgroundResource(R.drawable.ic_yes_no_left);
        negativeButton.setBackgroundResource(R.drawable.ic_yes_no_right);
    }

    public enum State {
        NEUTRAL, POSITIVE, NEGATIVE
    }

    public interface StateChangedListener {
        void onStateChanged(View view, State state);
    }
}
