package fm.doe.national.wash_core.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "geo")
public class Location implements Serializable {

    @Element
    public double longitude;

    @Element
    public double latitude;

    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
