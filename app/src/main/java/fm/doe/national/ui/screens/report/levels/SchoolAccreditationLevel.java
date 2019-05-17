package fm.doe.national.ui.screens.report.levels;

import java.util.List;

import fm.doe.national.app_support.utils.CollectionUtils;
import fm.doe.national.ui.screens.report.ReportLevel;

public class SchoolAccreditationLevel {

    private List<AccreditationForm> forms;
    private ReportLevel reportLevel;
    private float totalScore;
    private int totalObtainedScore;

    public static SchoolAccreditationLevel empty() {
        return new SchoolAccreditationLevel();
    }

    private SchoolAccreditationLevel() {
    }

    public SchoolAccreditationLevel(List<AccreditationForm> forms) {
        this.forms = forms;
        calculateTotalObtainedScore();
        calculateTotalScore();
        calculateReportLevel(this.totalScore);
    }

    private void calculateTotalObtainedScore() {
        Integer score = CollectionUtils.reduce(
                CollectionUtils.map(forms, AccreditationForm::getObtainedScore),
                (leftValue, rightValue) -> leftValue + rightValue
        );
        if (score != null) {
            totalObtainedScore = score;
        }
    }

    private void calculateTotalScore() {
        Float score = CollectionUtils.reduce(
                CollectionUtils.map(forms, AccreditationForm::getFinalScore),
                (leftValue, rightValue) -> leftValue + rightValue
        );
        if (score != null) {
            totalScore = score;
        }
    }

    private void calculateReportLevel(float score) {
        this.reportLevel = ReportLevel.estimateLevel(score);
    }

    public boolean isEmpty() {
        return forms == null;
    }

    public List<AccreditationForm> getForms() {
        return forms;
    }

    public ReportLevel getReportLevel() {
        return reportLevel;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public int getTotalObtainedScore() {
        return totalObtainedScore;
    }

}
