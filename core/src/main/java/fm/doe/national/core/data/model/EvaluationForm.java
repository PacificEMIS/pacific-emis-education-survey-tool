package fm.doe.national.core.data.model;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;

public enum EvaluationForm {
    CLASSROOM_OBSERVATION(Text.from(R.string.evaluation_form_school_evaluation)),
    SCHOOL_EVALUATION(Text.from(R.string.evaluation_form_classroom_observation));

    private Text name;

    EvaluationForm(Text name) {
        this.name = name;
    }

    public Text getName() {
        return name;
    }
}
