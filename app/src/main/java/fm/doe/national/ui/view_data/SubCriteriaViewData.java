package fm.doe.national.ui.view_data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;

public class SubCriteriaViewData {
    @Nullable
    private Answer correspondingAnswer;

    @NonNull
    private SubCriteria correspondingSubCriteria;

    private String text;
    private Answer.State answer;

    @NonNull
    public SubCriteria getCorrespondingSubCriteria() {
        return correspondingSubCriteria;
    }

    public void setCorrespondingSubCriteria(@NonNull SubCriteria correspondingSubCriteria) {
        this.correspondingSubCriteria = correspondingSubCriteria;
    }

    @Nullable
    public Answer getCorrespondingAnswer() {
        return correspondingAnswer;
    }

    public void setCorrespondingAnswer(@Nullable Answer correspondingAnswer) {
        this.correspondingAnswer = correspondingAnswer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Answer.State getAnswer() {
        return answer;
    }

    public void setAnswer(Answer.State answer) {
        this.answer = answer;
    }
}
