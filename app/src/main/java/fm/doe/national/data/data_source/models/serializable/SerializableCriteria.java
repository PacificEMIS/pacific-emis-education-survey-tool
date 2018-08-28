package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;

@Xml(name = "criteria")
public class SerializableCriteria implements Criteria, ListConverter.Converter<SubCriteria, SerializableSubCriteria> {

    @PropertyElement
    String name;

    @Element
    List<SerializableSubCriteria> subCriterias;

    public SerializableCriteria() {
    }

    public SerializableCriteria(Criteria criteria) {
        this.name = criteria.getName();
        this.subCriterias = ListConverter.createList(criteria.getSubCriterias(), this);
    }

    @Nullable
    @Override
    public Standard getStandard() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public SerializableSubCriteria convert(SubCriteria input) {
        return new SerializableSubCriteria(input.getName(), new SerializableAnswer(input.getAnswer()));
    }
}
