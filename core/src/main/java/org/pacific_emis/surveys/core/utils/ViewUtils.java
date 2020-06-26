package org.pacific_emis.surveys.core.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.omega_r.libs.omegatypes.image.Image;
import com.omega_r.libs.omegatypes.image.ImageKt;

import java.io.IOException;
import java.io.InputStream;

import org.pacific_emis.surveys.core.R;
import org.pacific_emis.surveys.core.data.model.Progress;

public class ViewUtils {

    private final static int MAX_COMPRESSED_IMAGE_DIMENSION = 1920;

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

    public static BitmapFactory.Options createBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return options;
    }

    public static void setImageTo(ImageView imageView, @Nullable Image image) {
        if (image != null) {
            ImageKt.setImage(imageView, image);
        }
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

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * MAX_COMPRESSED_IMAGE_DIMENSION x MAX_COMPRESSED_IMAGE_DIMENSION resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_COMPRESSED_IMAGE_DIMENSION, MAX_COMPRESSED_IMAGE_DIMENSION);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, options);

        bitmap = rotateImageIfRequired(context, bitmap, selectedImage);
        return bitmap;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth,
                                             int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param bitmap        The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface exifInterface = new ExifInterface(input);

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static void hideKeyboardAndClearFocus(View focusedView, View neutralFocusView) {
        InputMethodManager imm = (InputMethodManager) focusedView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
        neutralFocusView.requestFocus();
    }

}
