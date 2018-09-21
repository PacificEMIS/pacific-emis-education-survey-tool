package fm.doe.national.utils;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fm.doe.national.R;

public class ViewUtils {

    private static final SparseArray<Integer> STANDARD_ICONS = new SparseArray<>();
    static {
        STANDARD_ICONS.put(1, R.drawable.ic_standard_leadership_selector);
        STANDARD_ICONS.put(2, R.drawable.ic_standard_teacher_selector);
        STANDARD_ICONS.put(3, R.drawable.ic_standard_data_selector);
        STANDARD_ICONS.put(4, R.drawable.ic_standard_cirriculum_selector);
        STANDARD_ICONS.put(5, R.drawable.ic_standard_facility_selector);
        STANDARD_ICONS.put(6, R.drawable.ic_standard_improvement_selector);
        STANDARD_ICONS.put(7, R.drawable.ic_standard_observation_selector);
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
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        animation.setDuration(initialHeight / (long) view.getContext().getResources().getDisplayMetrics().density);
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
                        (interpolatedTime == 1) ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }
        };
        animation.setDuration(targetHeight / (long) view.getContext().getResources().getDisplayMetrics().density);
        view.startAnimation(animation);
    }

    public static void rebindProgress(int total,
                                      int done,
                                      @NonNull String progtessStringPattern,
                                      @Nullable TextView textView,
                                      @Nullable ProgressBar progressBar) {
        int progress = (int) ((float) done / total * 100);

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

    public static void setScaledDownImageTo(ImageView imageView, String imagePath) {
        int targetW = Constants.SIZE_THUMB_PICTURE;
        int targetH = Constants.SIZE_THUMB_PICTURE;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, bmOptions));
    }

    public static void setImageTo(ImageView imageView, String imagePath) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
    }

    @Nullable
    @DrawableRes
    public static Integer getStandardIconRes(@Nullable Integer id) {
        if (id == null) return null;
        return STANDARD_ICONS.get(id);
    }

}
