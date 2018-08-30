package fm.doe.national.data.data_source.models;

public interface CategoryProgress {
    int getTotalItemsCount();
    int getCompletedItemsCount();

    enum Action {
        INCREMENT,
        DECREMENT
    }
}
