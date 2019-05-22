package fm.doe.national.ui.screens.report.summary;

import java.util.List;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.ui.screens.report.ReportLevel;

public class SummaryViewData {

    private Category category;
    private Standard standard;
    private int totalByStandard;
    private List<CriteriaSummaryViewData> criteriaSummaryViewDataList;
    private ReportLevel level;

    public static SummaryViewData categoryOnly(Category category) {
        return new SummaryViewData(category, null, 0, 0, null);
    }

    public SummaryViewData(Category category,
                           Standard standard,
                           int totalByStandard,
                           int questionsCount,
                           List<CriteriaSummaryViewData> criteriaSummaryViewDataList) {
        this.category = category;
        this.standard = standard;
        this.totalByStandard = totalByStandard;
        this.criteriaSummaryViewDataList = criteriaSummaryViewDataList;
        if (questionsCount > 0) {
            this.level = ReportLevel.estimateLevel(totalByStandard, questionsCount);
        }
    }

    public boolean isCategoryOnly() {
        return standard == null;
    }

    public Category getCategory() {
        return category;
    }

    public Standard getStandard() {
        return standard;
    }

    public int getTotalByStandard() {
        return totalByStandard;
    }

    public List<CriteriaSummaryViewData> getCriteriaSummaryViewDataList() {
        return criteriaSummaryViewDataList;
    }

    public ReportLevel getLevel() {
        return level;
    }

    public static class CriteriaSummaryViewData {
        private String criteriaTitle;
        private boolean[] answerStates;
        private int total;

        public CriteriaSummaryViewData(String criteriaTitle, boolean[] answerStates, int total) {
            this.criteriaTitle = criteriaTitle;
            this.answerStates = answerStates;
            this.total = total;
        }

        public String getCriteriaTitle() {
            return criteriaTitle;
        }

        public boolean[] getAnswerStates() {
            return answerStates;
        }

        public int getTotal() {
            return total;
        }
    }
}
