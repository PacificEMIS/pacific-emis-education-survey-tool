package fm.doe.national.core.utils;


import androidx.annotation.NonNull;

import java.io.File;


public class TextUtil {
    private static final int ALPHABET_SIZE = 26;
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

    public static String getFileNameWithoutExtension(String path) {
        File file = new File(path);
        return file.getName().replaceFirst(REGEX_FILE_NAME, "");
    }

    @NonNull
    public static String repeat(@NonNull String string, int times) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < times; i++) {
            builder.append(string);
        }

        return builder.toString();
    }

}
