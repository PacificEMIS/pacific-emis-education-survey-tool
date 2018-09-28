package fm.doe.national.ui.custom_views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class SwitchableButton extends FrameLayout implements View.OnClickListener {

    @BindView(R.id.button_positive)
    Button positiveButton;

    @BindView(R.id.button_negative)
    Button negativeButton;

    private Resources resources = getResources();
    private Drawable drawableLeftActive;
    private Drawable drawableLeftInActive;
    private Drawable drawableRightActive;
    private Drawable drawableRightInActive;
    private int colorTextActive;
    private int colorTextInActive;

    private State state = State.NEUTRAL;

    @Nullable
    private StateChangedListener listener;

    public SwitchableButton(Context context) {
        this(context, null);
    }

    public SwitchableButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwitchableButton(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        inflate(context, R.layout.switch_answer, this);
        ButterKnife.bind(this);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        initColors();
    }

    private void initColors() {
        drawableLeftActive = resources.getDrawable(R.drawable.ic_yes_no_left_active);
        drawableLeftInActive = resources.getDrawable(R.drawable.ic_yes_no_left);
        drawableRightActive = resources.getDrawable(R.drawable.ic_yes_no_right_active);
        drawableRightInActive = resources.getDrawable(R.drawable.ic_yes_no_right);
        colorTextActive = resources.getColor(R.color.white);
        colorTextInActive = resources.getColor(R.color.grey_600);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_positive:
                setState(state == State.POSITIVE ? State.NEUTRAL : State.POSITIVE);
                break;
            case R.id.button_negative:
                setState(state == State.NEGATIVE ? State.NEUTRAL : State.NEGATIVE);
                break;
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
        positiveButton.setTextColor(colorTextActive);
        negativeButton.setTextColor(colorTextInActive);

        positiveButton.setBackground(drawableLeftActive);
        negativeButton.setBackground(drawableRightInActive);
    }

    private void becomeNegative() {
        positiveButton.setTextColor(colorTextInActive);
        negativeButton.setTextColor(colorTextActive);

        positiveButton.setBackground(drawableLeftInActive);
        negativeButton.setBackground(drawableRightActive);
    }

    private void becomeNeutral() {
        positiveButton.setTextColor(colorTextInActive);
        negativeButton.setTextColor(colorTextInActive);

        positiveButton.setBackground(drawableLeftInActive);
        negativeButton.setBackground(drawableRightInActive);
    }

    public enum State {
        NEUTRAL, POSITIVE, NEGATIVE
    }

    public interface StateChangedListener {
        void onStateChanged(SwitchableButton view, State state);
    }
}
