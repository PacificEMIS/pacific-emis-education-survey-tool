package fm.doe.national.data_source.static_source.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Accreditation implements Serializable {

    @Expose
    @SerializedName("group_standard")
    private List<StaticGroupStandard> list;

    public List<StaticGroupStandard> getList() {
        return list;
    }

}
