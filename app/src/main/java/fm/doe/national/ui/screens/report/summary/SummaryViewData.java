package fm.doe.national.ui.screens.report.summary;

import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Standard;

public class SummaryViewData {

    private Category category;
    private Standard standard;
    private int totalByStandard;
    private List<CriteriaSummaryViewData> criteriaSummaryViewDataList;

    public SummaryViewData(Category category, Standard standard, int totalByStandard, List<CriteriaSummaryViewData> criteriaSummaryViewDataList) {
        this.category = category;
        this.standard = standard;
        this.totalByStandard = totalByStandard;
        this.criteriaSummaryViewDataList = criteriaSummaryViewDataList;
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
