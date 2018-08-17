package fm.doe.national.mock;

import java.io.Serializable;
import java.util.List;

public class MockCriteria implements Serializable {
    private String name;
    private List<MockSubCriteria> subcriterias;

    public MockCriteria(String name, List<MockSubCriteria> subcriterias) {
        this.name = name;
        this.subcriterias = subcriterias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MockSubCriteria> getSubcriterias() {
        return subcriterias;
    }

    public void setSubcriterias(List<MockSubCriteria> subcriterias) {
        this.subcriterias = subcriterias;
    }

    public int getAnsweredCount() {
        int count = 0;
        for (MockSubCriteria question: subcriterias) {
            if (question.getState() != MockSubCriteria.State.NOT_ANSWERED) {
                count++;
            }
        }
        return count;
    }
}
