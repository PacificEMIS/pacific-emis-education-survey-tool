package fm.doe.national.mock;

import java.io.Serializable;
import java.util.List;
import fm.doe.national.ui.view_data.SubCriteriaViewData;

public class MockCriteria implements Serializable {
    private String name;
    private List<SubCriteriaViewData> subcriterias;

    public MockCriteria(String name, List<SubCriteriaViewData> subcriterias) {
        this.name = name;
        this.subcriterias = subcriterias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCriteriaViewData> getSubcriterias() {
        return subcriterias;
    }

    public void setSubcriterias(List<SubCriteriaViewData> subcriterias) {
        this.subcriterias = subcriterias;
    }

    public int getAnsweredCount() {
        int count = 0;
           for (SubCriteriaViewData subCriteria : subcriterias) {
                if (subCriteria.getAnswer() != fm.doe.national.data.data_source.models.Answer.State.NOT_ANSWERED) count++;
            }

        return count;
    }
}
