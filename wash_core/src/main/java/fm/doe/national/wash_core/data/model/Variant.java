package fm.doe.national.wash_core.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "variant")
public class Variant implements Serializable {

    @Element(name = "name")
    String name;

    @ElementList(entry = "variant_item", inline = true, required = false)
    List<VariantItem> variantItems;

    public String getName() {
        return name;
    }

    public List<VariantItem> getOptions() {
        return variantItems;
    }

}
