package fm.doe.national.ui.custom_views.summary;

import java.util.List;

import fm.doe.national.data.data_source.models.Category;
import fm.doe.national.data.data_source.models.Standard;

public class SummaryViewData {

    public Category category;
    public Standard standard;
    public List<Progress> progresses;

    public SummaryViewData(Category category, Standard standard, List<Progress> progresses) {
        this.category = category;
        this.standard = standard;
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
