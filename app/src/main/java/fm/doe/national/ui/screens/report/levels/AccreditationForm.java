package fm.doe.national.ui.screens.report.levels;

import com.omega_r.libs.omegatypes.Text;

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

        private Text name = Text.empty();
        private int obtainedScore = 0;
        private float multiplier = 0f;

        public AccreditationForm build() {
            return new AccreditationForm(name, obtainedScore, multiplier, obtainedScore * multiplier);
        }

        public Builder setName(Text name) {
            this.name = name;
            return this;
        }

        public Text getName() {
            return name;
        }

        public Builder addObtainedScore(int obtainedScore) {
            this.obtainedScore += obtainedScore;
            return this;
        }

        public Builder setMultiplier(float multiplier) {
            this.multiplier = multiplier;
            return this;
        }
    }
}