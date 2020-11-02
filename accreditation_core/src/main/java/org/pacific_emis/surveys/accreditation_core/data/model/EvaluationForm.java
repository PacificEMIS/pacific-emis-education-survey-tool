package org.pacific_emis.surveys.accreditation_core.data.model;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.R;

public enum EvaluationForm {
    SCHOOL_EVALUATION(Text.from(R.string.evaluation_form_school_evaluation)),
    CLASSROOM_OBSERVATION(Text.from(R.string.evaluation_form_classroom_observation));

    private Text name;

    EvaluationForm(Text name) {
        this.name = name;
    }

    public Text getName() {
        return name;
    }
}
