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
    private static final DateFormat uiDateFormat = new SimpleDateFormat("MM.dd.yyyy", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat uiTextDateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat monthYearDateFormat = new SimpleDateFormat("yyyy-MM", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat numericMonthYearDateFormat = new SimpleDateFormat("MM-yyyy", Locale.US);

    @SuppressLint("ConstantLocale")
    private static final DateFormat utcDateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss.SSS'Z'", Locale.US);

    static {
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String format(Date date) {
        return dateFormat.format(date);
    }

    public static String formatUi(Date date) {
        return uiDateFormat.format(date);
    }

    public static String formatUiText(Date date) {
        return uiTextDateFormat.format(date);
    }

    public static String formatMonthYear(Date date) {
        return monthYearDateFormat.format(date);
    }

    public static String formatNumericMonthYear(Date date) {
        return numericMonthYearDateFormat.format(date);
    }

    public static String formatUtc(Date date) {
        return utcDateFormat.format(date);
    }

    @Nullable
    public static Date parseUi(String dateAsString) {
        return parse(uiDateFormat, dateAsString);
    }

    @Nullable
    public static Date parseMonthYear(String dateAsString) {
        return parse(monthYearDateFormat, dateAsString);
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
