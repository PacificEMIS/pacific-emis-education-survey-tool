package fm.doe.national.core.utils;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    @SuppressLint("ConstantLocale")
    private static final DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat dateTagDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat numericMonthYearDateFormat = new SimpleDateFormat("MM-yyyy", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.US);

    static {
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String format(Date date) {
        return dateFormat.format(date);
    }

    public static String formatDateTag(Date date) {
        return dateTagDateFormat.format(date);
    }

    public static String formatNumericMonthYear(Date date) {
        return numericMonthYearDateFormat.format(date);
    }

    public static String formatUtc(Date date) {
        return utcDateFormat.format(date);
    }

    @Nullable
    public static Date parseDateTag(String dateAsString) {
        return parse(dateTagDateFormat, dateAsString);
    }

    @Nullable
    public static Date parseUtc(String dateAsString) {
        return parse(utcDateFormat, dateAsString);
    }

    @Nullable
    private static Date parse(DateFormat dateFormat, String dateAsString) {
        try {
            return dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            return null;
        }
    }

}
