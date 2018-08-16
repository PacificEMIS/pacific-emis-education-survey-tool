package fm.doe.national.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ViewUtils {

    public static void animateCollapsing(@NonNull View view) {
        animateCollapsing(view, null);
    }

    public static void animateCollapsing(@NonNull View view, @Nullable Runnable onAnimationEnd) {
        int initialHeight = view.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        animation.setDuration(initialHeight / (long)view.getContext().getResources().getDisplayMetrics().density);

        if (onAnimationEnd != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // nothing
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAnimationEnd.run();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // nothing
                }
            });
        }

        view.startAnimation(animation);
    }

    public static void animateExpanding(@NonNull View view) {
        animateExpanding(view, null);
    }

    public static void animateExpanding(@NonNull View view, @Nullable Runnable onAnimationEnd) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int targetHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height =
                        (interpolatedTime == 1) ? ViewGroup.LayoutParams.WRAP_CONTENT : (int)(targetHeight * interpolatedTime);
                view.requestLayout();
            }
        };
        animation.setDuration(targetHeight / (long)view.getContext().getResources().getDisplayMetrics().density);

        if (onAnimationEnd != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // nothing
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAnimationEnd.run();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // nothing
                }
            });
        }

        view.startAnimation(animation);

    }
}
