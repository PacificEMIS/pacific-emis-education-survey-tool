package fm.doe.national.utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JsonObjectsContainer<T extends Serializable> implements Serializable {

    @SerializedName("objects")
    private List<T> objects;

    public List<T> getObjects() {
        return objects;
    }
}
