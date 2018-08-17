package fm.doe.national.mock;

import android.support.annotation.Nullable;

import java.util.List;

public class MockCriteria {
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
}
