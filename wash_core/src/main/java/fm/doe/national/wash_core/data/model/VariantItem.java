package fm.doe.national.wash_core.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "variant_item")
public class VariantItem implements Serializable {

    @Element(name = "name")
    String name;

    @Element(name = "answer", required = false)
    String answer;

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
