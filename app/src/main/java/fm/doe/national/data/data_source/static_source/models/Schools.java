package fm.doe.national.data.data_source.static_source.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Schools implements Serializable {

    @Expose
    @SerializedName("schools")
    private List<StaticSchool> list;

    @NonNull
    public List<StaticSchool> getList() {
        return list == null ? Collections.EMPTY_LIST : list;
    }
}
