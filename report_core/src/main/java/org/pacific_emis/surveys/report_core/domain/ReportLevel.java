package org.pacific_emis.surveys.report_core.domain;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.report_core.R;


public enum ReportLevel implements BaseReportLevel {

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

    private final float maxValue;
    private final Text name;
    private final Text meaning;
    private final Text awards;
    @ColorRes
    private final int colorRes;

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

    @Override
    @NonNull
    public Text getName() {
        return name;
    }

    @Override
    public int getValue() {
        if (this == LEVEL_1) {
            return 1;
        } else if (this == LEVEL_2) {
            return 2;
        } else if (this == LEVEL_3) {
            return 3;
        } else {
            return 4;
        }
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
}
