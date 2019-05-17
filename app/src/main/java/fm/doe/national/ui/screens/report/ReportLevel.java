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

    private int max;
    private Text name;
    private Text determination;
    private Text determinationSource;

    @ColorRes
    private int colorRes;

    ReportLevel(int max, Text name, Text determination, Text determinationSource, int colorRes) {
        this.max = max;
        this.name = name;
        this.determination = determination;
        this.determinationSource = determinationSource;
        this.colorRes = colorRes;
    }

    public int getMax() {
        return max;
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
        return estimateLevel(MAX_LEVEL * actual / required);
    }

    public static ReportLevel estimateLevel(float level) {
        for (ReportLevel value : ReportLevel.values()) {
            if (level <= value.getMax()) {
                return value;
            }
        }
        throw new IllegalStateException("Impossible to estimate level (actual > required)");
    }
}
