package org.pacific_emis.surveys.report_core.domain;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.report_core.R;
import org.pacific_emis.surveys.report_core.model.Level;


public enum ReportLevel implements Level {

    LEVEL_1(
            50,
            Text.from(R.string.report_level_1),
            Text.from(R.string.report_level_1_determination),
            Text.from(R.string.report_level_1_determination_source),
            R.color.level_1
    ),
    LEVEL_2(
            75,
            Text.from(R.string.report_level_2),
            Text.from(R.string.report_level_2_determination),
            Text.from(R.string.report_level_2_determination_source),
            R.color.level_2
    ),
    LEVEL_3(
            90,
            Text.from(R.string.report_level_3),
            Text.from(R.string.report_level_3_determination),
            Text.from(R.string.report_level_3_determination_source),
            R.color.level_3
    ),
    LEVEL_4(
            100,
            Text.from(R.string.report_level_4),
            Text.from(R.string.report_level_4_determination),
            Text.from(R.string.report_level_4_determination_source),
            R.color.level_4
    );

    private static final float MAX_LEVEL = 100.0f;
    private static final float MIN_LEVEL = 0;
    private static final int VALUE_UNKNOWN = Integer.MAX_VALUE;

    private final float maxValue;
    private final Text name;
    private final Text meaning;
    private final Text awards;
    @ColorRes
    private final int colorRes;

    private float minValue = VALUE_UNKNOWN;

    ReportLevel(float maxValue, Text name, Text meaning, Text determinationSource, int colorRes) {
        this.maxValue = maxValue;
        this.name = name;
        this.meaning = meaning;
        this.awards = determinationSource;
        this.colorRes = colorRes;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        if (minValue == VALUE_UNKNOWN) {
            ReportLevel previousLevel = null;
            for (ReportLevel level : ReportLevel.values()) {
                if (level == this) {
                    minValue = previousLevel == null ? MIN_LEVEL : previousLevel.maxValue;
                }
                previousLevel = level;
            }
        }
        return minValue;
    }

    @Override
    @NonNull
    public Text getName() {
        return name;
    }

    @Override
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

    public static ReportLevel estimateLevel(float incomingLevel) {
        float level = Math.round(Math.round(incomingLevel * 10f) / 10f);
        for (ReportLevel value : ReportLevel.values()) {
            if (level <= value.getMaxValue()) {
                return value;
            }
        }
        throw new IllegalStateException("Impossible to estimate level (actual > required)");
    }
}
