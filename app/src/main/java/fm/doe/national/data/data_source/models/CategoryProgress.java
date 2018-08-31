package fm.doe.national.data.data_source.models;

public interface CategoryProgress {
    int getTotalQuestionsCount();
    int getAnsweredQuestionsCount();

    void recalculate(Answer.State previousState, Answer.State state);
}
