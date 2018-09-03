package fm.doe.national.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {
    public static DateFormat createDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }
}
