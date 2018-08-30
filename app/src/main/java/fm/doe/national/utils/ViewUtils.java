package fm.doe.national.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import fm.doe.national.R;

public class ViewUtils {

    public static final int[] STANDARD_ICONS = {
            R.drawable.ic_standard_leadership_selector,
            R.drawable.ic_standard_teacher_selector,
            R.drawable.ic_standard_data_selector,
            R.drawable.ic_standard_cirriculum_selector,
            R.drawable.ic_standard_facility_selector,
            R.drawable.ic_standard_improvement_selector,
            R.drawable.ic_standard_observation_selector
    };


    public static void animateCollapsing(@NonNull View view) {
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
        view.startAnimation(animation);
    }

    public static void animateExpanding(@NonNull View view) {
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
        view.startAnimation(animation);
    }

    public static void rebindProgress(int total,
                                      int done,
                                      @NonNull String progtessStringPattern,
                                      @Nullable TextView textView,
                                      @Nullable ProgressBar progressBar) {
        int progress = (int) ((float)done / total * 100);

        if (textView != null) {
            textView.setActivated(progress == 100);
            textView.setText(String.format(progtessStringPattern, done, total));
        }

        if (progressBar != null) {
            progressBar.setActivated(progress == 100);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }
        }
    }
}
