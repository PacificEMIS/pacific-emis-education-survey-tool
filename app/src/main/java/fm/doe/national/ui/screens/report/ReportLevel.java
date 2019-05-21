package fm.doe.national.ui.screens.report;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public enum ReportLevel {

    LEVEL_1(
            50,
            Text.from(R.string.report_level_1),
            Text.from(R.string.report_level_1_determination),
            Text.from(R.string.report_level_1_determination_source),
            R.color.red
    ),
    LEVEL_2(
            75,
            Text.from(R.string.report_level_2),
            Text.from(R.string.report_level_2_determination),
            Text.from(R.string.report_level_2_determination_source),
            R.color.yellow
    ),
    LEVEL_3(
            90,
            Text.from(R.string.report_level_3),
            Text.from(R.string.report_level_3_determination),
            Text.from(R.string.report_level_3_determination_source),
            R.color.light_mint
    ),
    LEVEL_4(
            100,
            Text.from(R.string.report_level_4),
            Text.from(R.string.report_level_4_determination),
            Text.from(R.string.report_level_4_determination_source),
            R.color.dark_mint);

    private static final float MAX_LEVEL = 100.0f;
    private static final int MIN_LEVEL = 0;
    private static final int VALUE_UNKNOWN = Integer.MAX_VALUE;

    private int maxValue;
    private int minValue = VALUE_UNKNOWN;
    private Text name;
    private Text meaning;
    private Text awards;

    @ColorRes
    private int colorRes;

    ReportLevel(int maxValue, Text name, Text meaning, Text determinationSource, int colorRes) {
        this.maxValue = maxValue;
        this.name = name;
        this.meaning = meaning;
        this.awards = determinationSource;
        this.colorRes = colorRes;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        if (minValue == VALUE_UNKNOWN) {
            ReportLevel previousLevel = null;
            for (ReportLevel level : ReportLevel.values()) {
                if (level == this) {
                    minValue = previousLevel == null ? MIN_LEVEL : (previousLevel.maxValue + 1);
                }
                previousLevel = level;
            }
        }
        return minValue;
    }

    @NonNull
    public Text getName() {
        return name;
    }

    @ColorRes
    public int getColorRes() {
        return colorRes;
    }

    @NonNull
    public Text getMeaning() {
        return meaning;
    }

    @NonNull
    public Text getAwards() {
        return awards;
    }

    public static ReportLevel estimateLevel(int actual, int required) {
        return estimateLevel(MAX_LEVEL * actual / required);
    }

    public static ReportLevel estimateLevel(float level) {
        for (ReportLevel value : ReportLevel.values()) {
            if (level <= value.getMaxValue()) {
                return value;
            }
        }
        throw new IllegalStateException("Impossible to estimate level (actual > required)");
    }
}
