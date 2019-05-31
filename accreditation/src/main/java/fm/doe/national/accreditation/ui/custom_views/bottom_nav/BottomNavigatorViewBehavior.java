package fm.doe.national.accreditation.ui.custom_views.bottom_nav;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class BottomNavigatorViewBehavior extends CoordinatorLayout.Behavior<BottomNavigatorView> {

    private static final long ANIMATION_DURATION = 200;

    private int height;

    public BottomNavigatorViewBehavior() {
    }

    public BottomNavigatorViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull BottomNavigatorView child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull BottomNavigatorView child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull BottomNavigatorView child,
                               @NonNull View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed,
                               int type,
                               @NonNull int[] consumed) {
        if (dyUnconsumed > 0) {
            slideUp(child, target);
        } else {
            slideDown(child, target);
        }
        Log.d("sd", "dxConsumed="+dxConsumed+" dyConsumed="+dyConsumed+" dxUnconsumed"+dxUnconsumed+" dyUnconsumed"+dyUnconsumed);
    }

    private void slideUp(BottomNavigatorView child, View target) {
        target.clearAnimation();
        child.clearAnimation();
        target.animate()
                .translationY(-height)
                .setDuration(ANIMATION_DURATION);
        child.animate()
                .translationY(0)
                .setDuration(ANIMATION_DURATION);
    }

    private void slideDown(BottomNavigatorView child, View target) {
        target.animate()
                .translationY(0)
                .setDuration(ANIMATION_DURATION)
                .start();
        child.animate()
                .translationY(height)
                .setDuration(ANIMATION_DURATION)
                .start();
    }
}