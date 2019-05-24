package fm.doe.national.rmi_report.model;

import androidx.annotation.Nullable;

import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.model.Level;

public class SchoolAccreditationTallyLevel {

    private int countOfOnes;
    private int countOfTwos;
    private int countOfThrees;
    private int countOfFours;
    private int tallyScore;

    @Nullable
    private Level level;

    public static SchoolAccreditationTallyLevel empty() {
        return new SchoolAccreditationTallyLevel(0, 0, 0, 0, 0);
    }

    public SchoolAccreditationTallyLevel(int countOfOnes,
                                         int countOfTwos,
                                         int countOfThrees,
                                         int countOfFours,
                                         int tallyScore) {
        this.countOfOnes = countOfOnes;
        this.countOfTwos = countOfTwos;
        this.countOfThrees = countOfThrees;
        this.countOfFours = countOfFours;
        this.tallyScore = tallyScore;
        this.level = calculateLevel();
    }

    public boolean isEmpty() {
        return level == null;
    }

    @Nullable
    private Level calculateLevel() {
        
        if (tallyScore == 0) {
            return null;
        }
        
        if (countOfFours >= countOfThrees && countOfFours >= countOfTwos && countOfOnes < 4) {
            return ReportLevel.LEVEL_4;
        }
        
        if (countOfThrees > countOfFours && countOfThrees >= countOfTwos && countOfOnes < 4) {
            return ReportLevel.LEVEL_3;
        }
        
        if (countOfTwos > countOfThrees && countOfTwos > countOfFours && countOfOnes < 4) {
            return ReportLevel.LEVEL_2;
        }

        return ReportLevel.LEVEL_1;
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
