package fm.doe.national.wash_core.data.serialization.model;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.io.Serializable;

@Root(name = "item")
public class Item implements Serializable {

    @Text
    String value;

    public String getValue() {
        return value;
    }
}
