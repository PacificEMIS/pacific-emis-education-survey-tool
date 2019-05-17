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
    LEVEL_3(new IntRange(76, 90),
            Text.from(R.string.report_level_3),
            Text.from(R.string.report_level_3_determination),
            Text.from(R.string.report_level_3_determination_source),
            R.color.light_mint
    ),
    LEVEL_4(new IntRange(90, 100),
            Text.from(R.string.report_level_4),
            Text.from(R.string.report_level_4_determination),
            Text.from(R.string.report_level_4_determination_source),
            R.color.dark_mint);

    private IntRange range;
    private Text name;
    private Text determination;
    private Text determinationSource;

    @ColorRes
    private int colorRes;

    ReportLevel(IntRange range, Text name, Text determination, Text determinationSource, int colorRes) {
        this.range = range;
        this.name = name;
        this.determination = determination;
        this.determinationSource = determinationSource;
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
    public Text getDetermination() {
        return determination;
    }

    @NonNull
    public Text getDeterminationSource() {
        return determinationSource;
    }

    public static ReportLevel estimateLevel(int actual, int required) {
        return estimateLevel(100.0f * actual / required);
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
