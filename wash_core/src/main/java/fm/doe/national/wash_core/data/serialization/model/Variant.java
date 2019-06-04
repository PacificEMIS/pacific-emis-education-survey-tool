package fm.doe.national.wash_core.data.serialization.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

@Root(name = "variant")
public class Variant implements Serializable {

    @Element(name = "name")
    String name;

    @ElementList(inline = true)
    ArrayList<Item> options; // AL for serialization purposes

    public String getName() {
        return name;
    }

    public ArrayList<Item> getOptions() {
        return options;
    }
}
