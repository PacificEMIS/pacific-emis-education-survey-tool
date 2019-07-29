package fm.doe.national.fsm_report.model;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.fsm_report.BuildConfig;

public class AccreditationForm {

    private final Text name;
    private final int obtainedScore;
    private final float finalScore;
    private final float multiplier;

    private AccreditationForm(Text name, int obtainedScore, float multiplier, float finalScore) {
        this.name = name;
        this.obtainedScore = obtainedScore;
        this.finalScore = finalScore;
        this.multiplier = multiplier;
    }

    public Text getName() {
        return name;
    }

    public int getObtainedScore() {
        return obtainedScore;
    }

    public float getFinalScore() {
        return finalScore;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public static class Builder {

        private int obtainedScore = 0;
        private int questionsCount = 0;
        private EvaluationForm form;

        public AccreditationForm build() {
            float multiplier = 0.0f;
            switch (form) {
                case CLASSROOM_OBSERVATION:
                    multiplier = BuildConfig.CLASSROOM_OBSERVATION_LEVEL_MULTIPLIER / questionsCount;
                    break;
                case SCHOOL_EVALUATION:
                    multiplier = BuildConfig.SCHOOL_EVALUATION_LEVEL_MULTIPLIER / questionsCount;
                    break;
            }
            return new AccreditationForm(form.getName(), obtainedScore, multiplier, obtainedScore * multiplier);
        }

        public Builder addObtainedScore(int obtainedScore) {
            this.obtainedScore += obtainedScore;
            return this;
        }

        public Builder addQuestionsCount(int count) {
            this.questionsCount += count;
            return this;
        }

        public Builder setForm(EvaluationForm form) {
            this.form = form;
            return this;
        }

        public EvaluationForm getForm() {
            return form;
        }

    }
}
