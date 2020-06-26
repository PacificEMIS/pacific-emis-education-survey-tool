package org.pacific_emis.surveys.wash_core.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.pacific_emis.surveys.core.utils.CollectionUtils;

@Root(name = "variant")
public class Variant implements Serializable {

    @Element(name = "name")
    String name;

    @ElementList(entry = "variant_item", inline = true, required = false)
    List<VariantItem> variantItems;

    public static Variant copy(Variant other) {
        Variant variant = new Variant();
        variant.name = other.name;
        variant.variantItems = other.variantItems == null ? null : new ArrayList<>(other.variantItems);
        return variant;
    }

    public String getName() {
        return name;
    }

    public List<VariantItem> getOptions() {
        return variantItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return Objects.equals(name, variant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public boolean isBlocker() {
        return CollectionUtils.isEmpty(variantItems);
    }
}
