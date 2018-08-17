package fm.doe.national.mock;

import android.support.annotation.DrawableRes;

import java.io.Serializable;
import java.util.List;

public class MockStandard implements Serializable{
    private String name;
    private List<MockCriteria> criterias;
    private @DrawableRes int icon;
    private @DrawableRes int iconHighlighted;

    public MockStandard(String name, List<MockCriteria> criterias) {
        this.name = name;
        this.criterias = criterias;
    }

    public MockStandard() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MockCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<MockCriteria> criterias) {
        this.criterias = criterias;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public int getIconHighlighted() {
        return iconHighlighted;
    }

    public void setIconHighlighted(int iconHighlighted) {
        this.iconHighlighted = iconHighlighted;
    }

    public int getAnsweredCount() {
        int count = 0;
        for (MockCriteria criteria: criterias) {
            count += criteria.getAnsweredCount();
        }
        return count;
    }

    public int getQuestionsCount() {
        int count = 0;
        for (MockCriteria criteria: criterias) {
            count += criteria.getSubcriterias().size();
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return ((MockStandard)obj).getName().equals(name);
        } catch (ClassCastException ex) {
            return false;
        }
    }
}
