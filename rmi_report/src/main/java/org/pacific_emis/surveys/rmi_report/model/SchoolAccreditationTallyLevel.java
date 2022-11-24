package org.pacific_emis.surveys.rmi_report.model;

import androidx.annotation.Nullable;

import org.pacific_emis.surveys.rmi_report.domain.RmiReportLevel;
import org.pacific_emis.surveys.report_core.model.Level;

public class SchoolAccreditationTallyLevel {

    public static final int MAX_CRITERIA_SUM = 4;
    private static final int MAX_ONES_COUNT = 4;

    private int countOfOnes;
    private int countOfTwos;
    private int countOfThrees;
    private int countOfFours;
    private int tallyScore;

    @Nullable
    private Level level;

    public static SchoolAccreditationTallyLevel empty() {
        return new SchoolAccreditationTallyLevel();
    }

    public SchoolAccreditationTallyLevel(int[] counts) {
        if (counts.length != MAX_CRITERIA_SUM) {
            throw new IllegalStateException();
        }
        this.countOfOnes = counts[0];
        this.countOfTwos = counts[1];
        this.countOfThrees = counts[2];
        this.countOfFours = counts[3];
        this.tallyScore = countOfOnes + countOfTwos + countOfThrees + countOfFours;
        this.level = calculateLevel();
    }

    private SchoolAccreditationTallyLevel() {
        // nothing
    }

    public boolean isEmpty() {
        return level == null;
    }

    @Nullable
    private Level calculateLevel() {
        
        if (tallyScore == 0) {
            return null;
        }

        if (countOfFours >= countOfThrees && countOfFours >= countOfTwos && countOfOnes <= MAX_ONES_COUNT) {
            return RmiReportLevel.LEVEL_4;
        }

        if (countOfThrees > countOfFours && countOfThrees >= countOfTwos && countOfOnes <= MAX_ONES_COUNT) {
            return RmiReportLevel.LEVEL_3;
        }

        if (countOfTwos > countOfThrees && countOfTwos > countOfFours && countOfOnes <= MAX_ONES_COUNT) {
            return RmiReportLevel.LEVEL_2;
        }

        return RmiReportLevel.LEVEL_1;
    }

    public int getCountOfOnes() {
        return countOfOnes;
    }

    public int getCountOfTwos() {
        return countOfTwos;
    }

    public int getCountOfThrees() {
        return countOfThrees;
    }

    public int getCountOfFours() {
        return countOfFours;
    }

    public int getTallyScore() {
        return tallyScore;
    }

    @Nullable
    public Level getLevel() {
        return level;
    }
}
