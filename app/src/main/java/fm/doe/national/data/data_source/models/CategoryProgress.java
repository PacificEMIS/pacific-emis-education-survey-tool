package fm.doe.national.data.data_source.models;

public interface CategoryProgress {
    int getTotalItemsCount();
    int getCompletedItemsCount();

    void recalculate(Answer.State previousState, Answer.State state);
}
