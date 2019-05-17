package fm.doe.national.ui.screens.report;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;
import fm.doe.national.app_support.utils.IntRange;

public enum ReportLevel {

    LEVEL_1(
            new IntRange(0, 50),
            Text.from(R.string.report_level_1),
            Text.from(R.string.report_level_1_determination),
            Text.from(R.string.report_level_1_determination_source),
            R.color.red
    ),
    LEVEL_2(
            new IntRange(51, 75),
            Text.from(R.string.report_level_2),
            Text.from(R.string.report_level_2_determination),
            Text.from(R.string.report_level_2_determination_source),
            R.color.yellow
    ),
    LEVEL_3(
            new IntRange(76, 90),
            Text.from(R.string.report_level_3),
            Text.from(R.string.report_level_3_determination),
            Text.from(R.string.report_level_3_determination_source),
            R.color.light_mint
    ),
    LEVEL_4(
            new IntRange(91, 100),
            Text.from(R.string.report_level_4),
            Text.from(R.string.report_level_4_determination),
            Text.from(R.string.report_level_4_determination_source),
            R.color.dark_mint);

    private static final float MAX_LEVEL = 100.0f;

    private IntRange range;
    private Text name;
    private Text meaning;
    private Text awards;

    @ColorRes
    private int colorRes;

    ReportLevel(IntRange range, Text name, Text meaning, Text determinationSource, int colorRes) {
        this.range = range;
        this.name = name;
        this.meaning = meaning;
        this.awards = determinationSource;
        this.colorRes = colorRes;
    }

    @NonNull
    public IntRange getRange() {
        return range;
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
            if (value.getRange().containsValue(level)) {
                return value;
            }
        }
        throw new IllegalStateException("Impossible to estimate level (actual > required)");
    }
}
