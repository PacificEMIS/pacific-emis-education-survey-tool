package fm.doe.national.ui.custom_views.summary;

import java.util.List;

public class SummaryViewData {
    public String category;
    public List<Standard> standards;

    public SummaryViewData(String category, List<Standard> standards) {
        this.category = category;
        this.standards = standards;
    }

    public class Standard {
        public String name;
        public Progress firstCatregoryProgress;
        public Progress secondCatregoryProgress;
        public Progress thirdCatregoryProgress;
        public Progress forthCatregoryProgress;

        public Standard(String name,
                        Progress firstCatregoryProgress,
                        Progress secondCatregoryProgress,
                        Progress thirdCatregoryProgress,
                        Progress forthCatregoryProgress) {
            this.name = name;
            this.firstCatregoryProgress = firstCatregoryProgress;
            this.secondCatregoryProgress = secondCatregoryProgress;
            this.thirdCatregoryProgress = thirdCatregoryProgress;
            this.forthCatregoryProgress = forthCatregoryProgress;
        }

        public class Progress {
            public int total;
            public int completed;

            public Progress(int total, int completed) {
                this.total = total;
                this.completed = completed;
            }
        }
    }
}
