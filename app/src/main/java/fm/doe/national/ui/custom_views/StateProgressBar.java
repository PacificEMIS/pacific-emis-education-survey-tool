package fm.doe.national.ui.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

import fm.doe.national.R;

public class StateProgressBar extends ProgressBar {

    private Drawable backgroundDrawable;
    private Drawable progressDrawable;
    private Drawable successDrawable;

    private LayerDrawable defaultProgressDrawable;


    public StateProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs, 0);
    }

    public StateProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attributeSet, int defStyleAttr) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.StateProgressBar, defStyleAttr, 0);
        setBackgroundProgressDrawable(styledAttributes.getDrawable(R.styleable.StateProgressBar_bgColor));
        setProgressStateDrawable(styledAttributes.getDrawable(R.styleable.StateProgressBar_progressColor));
        setSuccessStateDrawable(styledAttributes.getDrawable(R.styleable.StateProgressBar_successColor));
        defaultProgressDrawable = (LayerDrawable) getProgressDrawable();
        styledAttributes.recycle();
    }

    public void setBackgroundProgressDrawable(Drawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
        invalidate();
    }

    public void setSuccessStateDrawable(Drawable successDrawable) {
        this.successDrawable = successDrawable;
        invalidate();
    }

    public void setProgressStateDrawable(Drawable progressDrawable) {
        this.progressDrawable = new ClipDrawable(progressDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        invalidate();
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public Drawable getProgressStateDrawable() {
        return progressDrawable;
    }

    public Drawable getSuccessStateDrawable() {
        return successDrawable;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if (!isIndeterminate() && needToDrawCustomProgress()) {
            updateVisualState();
        }
    }

    private boolean needToDrawCustomProgress() {
        return getProgressStateDrawable() != null;
    }

    private void updateVisualState() {
        LayerDrawable currentStateDrawables;

        Drawable backgroundDrawable = getBackgroundDrawable() == null ? getDefaultBackgroundDrawable() : getBackgroundDrawable();

        if (getProgress() == getMax()) {
            Drawable currentStateDrawable = getSuccessStateDrawable() == null ? getProgressStateDrawable() : getSuccessStateDrawable();
            currentStateDrawables = new LayerDrawable(new Drawable[]{backgroundDrawable, currentStateDrawable});
        } else {
            currentStateDrawables = new LayerDrawable(new Drawable[]{backgroundDrawable, getProgressStateDrawable()});
        }

        currentStateDrawables.setId(0, android.R.id.background);
        currentStateDrawables.setId(1, android.R.id.progress);

        setProgressDrawable(currentStateDrawables);
    }

    private Drawable getDefaultBackgroundDrawable() {
        return defaultProgressDrawable.getDrawable(0);
    }
}
