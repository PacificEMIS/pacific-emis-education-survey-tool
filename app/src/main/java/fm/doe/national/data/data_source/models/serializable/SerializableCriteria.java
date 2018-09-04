package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;

@Root(name = "criteria")
public class SerializableCriteria implements Criteria, ListConverter.Converter<SubCriteria, SerializableSubCriteria> {

    @Element
    String name;

    @ElementList(inline = true)
    List<SerializableSubCriteria> subCriterias;

    public SerializableCriteria() {
    }

    public SerializableCriteria(Criteria criteria) {
        this.name = criteria.getName();
        this.subCriterias = ListConverter.createList(criteria.getSubCriterias(), this);
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
