package fm.doe.national.core.utils;


import androidx.annotation.NonNull;

import java.io.File;


public class TextUtil {
    private static final int ALPHABET_SIZE = 24;
    private static final char FIRST_ALPHABET_CHAR = 'a';
    private static final String REGEX_FILE_NAME = "[.][^.]+$";

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

        builder.append((char) (FIRST_ALPHABET_CHAR + mod));
    }

    public static boolean endsWith(String source, String suffix) {
        if (source.length() < suffix.length()) {
            return false;
        }

        String sourceEnding = source.substring(source.length() - suffix.length());
        return sourceEnding.equals(suffix);
    }

    public static boolean startsWith(String source, String prefix) {
        if (source.length() < prefix.length()) {
            return false;
        }

        String sourceStarting = source.substring(0, prefix.length());
        return sourceStarting.equals(prefix);
    }

    public static String getFileNameWithoutExtension(String path) {
        File file = new File(path);
        return file.getName().replaceFirst(REGEX_FILE_NAME, "");
    }

}
