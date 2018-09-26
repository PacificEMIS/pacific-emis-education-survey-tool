package fm.doe.national.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
    private static final DateFormat monthYearDateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    public static String format(Date date) {
        return dateFormat.format(date);
    }

    public static String formatMonthYear(Date date) {
        return monthYearDateFormat.format(date);
    }
}
