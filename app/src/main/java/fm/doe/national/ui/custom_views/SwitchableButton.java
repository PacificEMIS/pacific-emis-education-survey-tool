package fm.doe.national.ui.custom_views;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    
    private Drawable drawableLeftActive;
    private Drawable drawableLeftInActive;
    private Drawable drawableRightActive;
    private Drawable drawableRightInActive;
    private int colorTextActive;
    private int colorTextInActive;

    private State state;
    private StateChangedListener listener;

    public SwitchableButton(Context context) {
        super(context, null);
    }

    public SwitchableButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public SwitchableButton(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        inflate(getContext(), R.layout.switch_answer, this);
        ButterKnife.bind(this);

        state = State.NEUTRAL;

        positiveButton.setOnClickListener(v -> setState(state == State.POSITIVE ? State.NEUTRAL : State.POSITIVE));
        negativeButton.setOnClickListener(v -> setState(state == State.NEGATIVE ? State.NEUTRAL : State.NEGATIVE));

        initColors();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
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

    private void initColors() {
        drawableLeftActive = getResources().getDrawable(R.drawable.ic_yes_no_left_active);
        drawableLeftInActive = getResources().getDrawable(R.drawable.ic_yes_no_left);
        drawableRightActive = getResources().getDrawable(R.drawable.ic_yes_no_right_active);
        drawableRightInActive = getResources().getDrawable(R.drawable.ic_yes_no_right);
        colorTextActive = getResources().getColor(R.color.white);
        colorTextInActive = getResources().getColor(R.color.black);
    }

    private void becomePositive() {
        positiveButton.setTextColor(colorTextActive);
        negativeButton.setTextColor(colorTextInActive);

        positiveButton.setBackground(drawableLeftActive);
        negativeButton.setBackground(drawableRightInActive);
    }

    private void becomeNegative() {
        positiveButton.setTextColor(getResources().getColor(R.color.black));
        negativeButton.setTextColor(getResources().getColor(R.color.white));

        positiveButton.setBackground(drawableLeftInActive);
        negativeButton.setBackground(drawableRightActive);
    }

    private void becomeNeutral() {
        positiveButton.setTextColor(getResources().getColor(R.color.black));
        negativeButton.setTextColor(getResources().getColor(R.color.black));

        positiveButton.setBackground(drawableLeftInActive);
        negativeButton.setBackground(drawableRightInActive);
    }

    public enum State {
        NEUTRAL, POSITIVE, NEGATIVE
    }

    public interface StateChangedListener {
        void onStateChanged(View view, State state);
    }
}
