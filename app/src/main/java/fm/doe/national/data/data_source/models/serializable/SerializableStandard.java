package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;


import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

@Xml(name = "standard")
public class SerializableStandard implements Standard {

    @PropertyElement
    private String name;

    @Element
    List<SerializableCriteria> criterias;

    @Nullable
    @Override
    public GroupStandard getGroupStandard() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends Criteria> getCriterias() {
        return criterias;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCriterias(List<SerializableCriteria> criterias) {
        this.criterias = criterias;
    }
}
