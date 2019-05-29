package fm.doe.national.core.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import java.util.Date;

import fm.doe.national.core.data.model.School;

public class TextUtil {
    private static final int ALPHABET_SIZE = 24;

    @NonNull
    public static String convertIntToCharsIcons(int position) {
        if (position < 0) throw new RuntimeException("Position cannot be less then 0");
        StringBuilder builder = new StringBuilder();
        fillBuilderWithCharNumbers(position, builder);
        return builder.toString();
    }

    private static void fillBuilderWithCharNumbers(int number, StringBuilder builder) {
        int offset = 1;
        int div = number / ALPHABET_SIZE;
        int mod = number % ALPHABET_SIZE;

        if (div > 0) {
            fillBuilderWithCharNumbers(div - offset, builder);
        }

        builder.append((char)('a' + mod));
    }

    @NonNull
    public static String createSurveyFileName(School school, Date date) {
        return school.getName() + '-' + school.getId() + '-' + DateUtils.format(date) + ".xml";
    }

    public static SpannableString createSpannableString(Context context,
                                                        Text textToSpan,
                                                        Text delimeterText,
                                                        Text simpleText,
                                                        @Nullable Typeface spanTypeface) {
        String stringToSpan = textToSpan.getString(context);
        String delimeter = delimeterText.getString(context);
        String simpleString = simpleText.getString(context);
        SpannableString spannableString = new SpannableString(stringToSpan + delimeter + simpleString);

        if (spanTypeface != null) {
            spannableString.setSpan(
                    new StyleSpan(spanTypeface.getStyle()),
                    0,
                    stringToSpan.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return spannableString;
    }
}
