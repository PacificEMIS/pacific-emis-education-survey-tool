package fm.doe.national.ui.custom_views.summary;

import java.util.List;

public class SummaryViewData {
    public String category;
    public List<Standard> standards;

    public SummaryViewData(String category, List<Standard> standards) {
        this.category = category;
        this.standards = standards;
    }

    public static class Standard {
        public String name;
        public List<Progress> progresses;

        public Standard(String name, List<Progress> progresses) {
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
}
