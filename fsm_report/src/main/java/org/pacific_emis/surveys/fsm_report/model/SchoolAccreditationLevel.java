package org.pacific_emis.surveys.fsm_report.model;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.fsm_report.domain.FsmReportLevel;

import java.util.Collections;
import java.util.List;

public class SchoolAccreditationLevel {

    private List<AccreditationForm> forms = Collections.emptyList();
    private FsmReportLevel reportLevel;
    private float totalScore;
    private int totalObtainedScore;

    public static SchoolAccreditationLevel empty() {
        return new SchoolAccreditationLevel();
    }

    private SchoolAccreditationLevel() {
    }

    public SchoolAccreditationLevel(@NonNull List<AccreditationForm> forms) {
        this.forms = forms;
        calculateTotalObtainedScore();
        calculateTotalScore();
        calculateReportLevel(this.totalScore);
    }

    public boolean isEmpty() {
        return reportLevel == null;
    }

    private void calculateTotalObtainedScore() {
        forms.parallelStream()
                .map(AccreditationForm::getObtainedScore)
                .reduce((leftValue, rightValue) -> leftValue + rightValue)
                .ifPresent(value -> totalObtainedScore = value);
    }

    private void calculateTotalScore() {
        forms.parallelStream()
                .map(AccreditationForm::getFinalScore)
                .reduce((leftValue, rightValue) -> leftValue + rightValue)
                .ifPresent(value -> totalScore = value);
    }

    private void calculateReportLevel(float score) {
        this.reportLevel = FsmReportLevel.estimateLevel(score);
    }

    public List<AccreditationForm> getForms() {
        return forms;
    }

    public FsmReportLevel getReportLevel() {
        return reportLevel;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public int getTotalObtainedScore() {
        return totalObtainedScore;
    }

}
