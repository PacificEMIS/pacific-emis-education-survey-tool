package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;

@Xml(name = "standard")
public class SerializableStandard implements LinkedStandard, ListConverter.Converter<Criteria, SerializableCriteria> {

    @PropertyElement
    String name;

    @Element
    List<SerializableCriteria> criterias;

    public SerializableStandard() {
    }

    public SerializableStandard(LinkedStandard standard) {
        this.name = standard.getName();
        this.criterias = ListConverter.createList(standard.getCriterias(), this);
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public CategoryProgress getCategoryProgress() {
        return null;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public SerializableCriteria convert(Criteria input) {
        return new SerializableCriteria(input);
    }

    @Override
    public List<? extends Criteria> getCriterias() {
        return criterias;
    }

}
