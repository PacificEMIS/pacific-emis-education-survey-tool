package org.pacific_emis.surveys.fsm_report.levels;

import androidx.annotation.NonNull;

import java.util.Locale;

class EvaluationFormFormatter {

    @NonNull
    static String formatTotalScore(float score) {
        return String.format(Locale.ROOT, "%.1f", score); // float ',' is forced to be '.' in ROOT locale
    }

}
