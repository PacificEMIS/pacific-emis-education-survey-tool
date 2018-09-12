package fm.doe.national.ui.custom_views.summary;

import java.util.List;

import fm.doe.national.data.data_source.models.Category;

public class SummaryViewData {

    public Category category;
    public String name;
    public List<Progress> progresses;

    public SummaryViewData(Category category, String name, List<Progress> progresses) {
        this.category = category;
        this.name = name;
        this.progresses = progresses;
    }

    public static class Progress {
        public int total;
        public int completed;

        public Progress(int total, int completed) {
            this.total = total;
            this.completed = completed;
        }
    }
}
