package fm.doe.national.wash_core.data.serialization.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "variant")
public class Variant implements Serializable {

    @Element(name = "name")
    String name;

    @ElementList(entry = "item", inline = true, required = false)
    List<String> options;

    public String getName() {
        return name;
    }

    public List<String> getOptions() {
        return options;
    }
}
