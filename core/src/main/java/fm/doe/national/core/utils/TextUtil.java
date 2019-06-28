package fm.doe.national.core.utils;


import androidx.annotation.NonNull;

import java.io.File;


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

        builder.append((char) ('a' + mod));
    }

    public static boolean endsWith(String source, String ending) {
        if (source.length() < ending.length()) {
            return false;
        }

        String sourceEnding = source.substring(source.length() - ending.length());
        return sourceEnding.equals(ending);
    }

    public static String getFileNameWithoutExtension(String path) {
        File file = new File(path);
        return file.getName().replaceFirst("[.][^.]+$", "");
    }

}
