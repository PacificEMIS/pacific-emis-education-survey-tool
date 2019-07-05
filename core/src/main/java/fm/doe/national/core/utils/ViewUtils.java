package fm.doe.national.core.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import fm.doe.national.core.R;
import fm.doe.national.core.data.model.Progress;

public class ViewUtils {

    public static void rebindProgress(Progress progressData,
                                      @Nullable TextView textView,
                                      @Nullable ProgressBar progressBar) {
        int done = progressData.getCompleted();
        int total = progressData.getTotal();
        int progress = (int) ((float) done / total * 100);

        if (textView != null) {
            textView.setActivated(progress == 100);
            textView.setText(textView.getContext().getString(R.string.criteria_progress, done, total));
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

        BitmapFactory.Options bmOptions = createBitmapOptions();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, bmOptions));
    }

    public static BitmapFactory.Options createBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return options;
    }

    public static void setImageTo(ImageView imageView, String imagePath) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, createBitmapOptions()));
    }

    public static void setTintedBackgroundDrawable(View targetView, @DrawableRes int drawableRes, @ColorRes int colorRes) {
        Context context = targetView.getContext();
        Drawable backgroundDrawable = ContextCompat.getDrawable(context, drawableRes);

        if (backgroundDrawable != null) {
            backgroundDrawable = DrawableCompat.wrap(backgroundDrawable);
            DrawableCompat.setTint(
                    backgroundDrawable.mutate(),
                    ContextCompat.getColor(context, colorRes)
            );
            targetView.setBackground(backgroundDrawable);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
