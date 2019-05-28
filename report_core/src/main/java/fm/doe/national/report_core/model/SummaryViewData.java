package fm.doe.national.report_core.model;

import java.util.List;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Standard;

public class SummaryViewData {

    private static final int ANSWER_COUNT_FOR_LONG_LAYOUT_TYPE = 5;

    private Category category;
    private Standard standard;
    private int totalByStandard;
    private List<CriteriaSummaryViewData> criteriaSummaryViewDataList;
    private Level level;
    private LayoutType layoutType;

    public static SummaryViewData categoryOnly(Category category, LayoutType layoutType) {
        return new SummaryViewData(category, layoutType);
    }

    private SummaryViewData(Category category, LayoutType layoutType) {
        this.category = category;
        this.layoutType = layoutType;
    }

    public SummaryViewData(Category category,
                           Standard standard,
                           int totalByStandard,
                           List<CriteriaSummaryViewData> criteriaSummaryViewDataList,
                           Level level) {
        this.category = category;
        this.standard = standard;
        this.totalByStandard = totalByStandard;
        this.criteriaSummaryViewDataList = criteriaSummaryViewDataList;
        this.level = level;
        LayoutType layoutType = LayoutType.SHORT;
        if (this.criteriaSummaryViewDataList.size() > 0 &&
                this.criteriaSummaryViewDataList.get(0).getAnswerStates().length == ANSWER_COUNT_FOR_LONG_LAYOUT_TYPE) {
            layoutType = LayoutType.LONG;
        }
        this.layoutType = layoutType;
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

    public Level getLevel() {
        return level;
    }

    public LayoutType getLayoutType() {
        return layoutType;
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

    public enum LayoutType {
        LONG, SHORT
    }
}
