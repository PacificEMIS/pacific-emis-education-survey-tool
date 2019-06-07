package fm.doe.national.wash_core.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Objects;

@Root(name = "variant_item")
public class VariantItem implements Serializable {

    @Element(name = "name")
    String name;

    @Element(name = "answer", required = false)
    String answer;

    public static VariantItem copy(VariantItem other) {
        VariantItem item = new VariantItem();
        item.name = other.name;
        item.answer = other.answer;
        return item;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantItem that = (VariantItem) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
