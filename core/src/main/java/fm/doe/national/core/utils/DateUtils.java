package fm.doe.national.core.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    @SuppressLint("ConstantLocale")
    private static final DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());

    @SuppressLint("ConstantLocale")
    private static final DateFormat uiDateFormat = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());

    @SuppressLint("ConstantLocale")
    private static final DateFormat uiTextDateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());

    @SuppressLint("ConstantLocale")
    private static final DateFormat monthYearDateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

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
    
}
