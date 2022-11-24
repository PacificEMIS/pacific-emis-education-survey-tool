package org.pacific_emis.surveys.report_core.domain;


import org.pacific_emis.surveys.report_core.model.Level;

public interface BaseReportLevel extends Level {

    float MAX_LEVEL = 100.0f;
    float MIN_LEVEL = 0;

    float getMaxValue();

    default float getMinValue() {
        ReportLevel previousLevel = null;
        for (ReportLevel level : ReportLevel.values()) {
            if (level == this) {
                return previousLevel == null ? MIN_LEVEL : previousLevel.getMaxValue();
            }
            previousLevel = level;
        }
        return Float.MAX_VALUE;
    }

    static ReportLevel estimateLevel ( int actual, int required){
        return estimateLevel(MAX_LEVEL * actual / required);
    }

    static ReportLevel estimateLevel ( float incomingLevel){
        float level = Math.round(Math.round(incomingLevel * 10f) / 10f);
        for (ReportLevel value : ReportLevel.values()) {
            if (level <= value.getMaxValue()) {
                return value;
            }
        }
        throw new IllegalStateException("Impossible to estimate level (actual > required)");
    }

}

