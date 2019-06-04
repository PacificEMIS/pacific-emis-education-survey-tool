package fm.doe.national.wash_core.data.serialization.model;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "relationAnswer")
public class RelationAnswer {

    @Text
    String value;

    public String getValue() {
        return value;
    }
}
