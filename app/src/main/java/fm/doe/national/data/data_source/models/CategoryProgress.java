package fm.doe.national.data.data_source.models;

public interface CategoryProgress {
    int getTotalItemsCount();
    int getCompletedItemsCount();

    void incrementCompletedItems();

    void decrementCompletedItems();

    enum Action {
        INCREMENT,
        DECREMENT
    }
}
