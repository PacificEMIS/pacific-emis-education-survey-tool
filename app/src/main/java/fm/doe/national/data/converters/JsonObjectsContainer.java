package fm.doe.national.data.converters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

public class JsonObjectsContainer<T extends Serializable> implements Serializable {

    @SerializedName("objects")
    private Collection<T> objects;

    public Collection<T> getObjects() {
        return objects;
    }
}
