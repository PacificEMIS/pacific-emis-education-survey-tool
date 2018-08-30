package fm.doe.national.ui.view_data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;

public class CriteriaViewData {

    @NonNull
    private Criteria correspondingCriteria;

    private String name;
    private List<SubCriteriaViewData> questions;

    private CriteriaViewData(@NonNull Criteria criteria) {
        correspondingCriteria = criteria;
        this.name = criteria.getName();
        this.questions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCriteriaViewData> getQuestionsViewData() {
        return questions;
    }

    public void setQuestions(List<SubCriteriaViewData> questions) {
        this.questions = questions;
    }

    @NonNull
    public Criteria getCorrespondingCriteria() {
        return correspondingCriteria;
    }

    public int getAnsweredCount() {
        int count = 0;
        for (SubCriteriaViewData subCriteriaViewData: questions) {
            if (subCriteriaViewData.getAnswer() != Answer.State.NOT_ANSWERED) count++;
        }
        return count;
    }

    public int getPercentageProgress() {
        int totalQuestions = questions.size();
        int answeredQuestions = getAnsweredCount();
        return totalQuestions > 0 ? (int)((float)answeredQuestions / totalQuestions * 100) : 0;
    }

    public static class Builder {
        private CriteriaViewData object;

        public Builder(@NonNull Criteria criteria) {
            object = new CriteriaViewData(criteria);
        }

        public Builder addQuestion(SubCriteria subCriteria, @Nullable Answer answer) {
            SubCriteriaViewData question = new SubCriteriaViewData();
            question.setCorrespondingSubCriteria(subCriteria);
            question.setCorrespondingAnswer(answer);
            question.setText(subCriteria.getName());
            question.setAnswer(answer == null ? Answer.State.NOT_ANSWERED
                    : answer.getState());
            object.questions.add(question);
            return this;
        }

        public CriteriaViewData build() {
            return object;
        }
    }
}
