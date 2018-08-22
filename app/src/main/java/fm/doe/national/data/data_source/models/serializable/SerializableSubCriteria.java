package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;

@Xml(name = "subcriteria")
public class SerializableSubCriteria implements SubCriteria {

    @PropertyElement
    String name;

    @Nullable
    @Override
    public Criteria getCriteria() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }
}
