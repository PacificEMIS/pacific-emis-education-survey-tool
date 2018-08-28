package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

@Xml(name = "standard")
public class SerializableStandard implements Standard, ListConverter.Converter<Criteria, SerializableCriteria> {

    @PropertyElement
    String name;

    @Element
    List<SerializableCriteria> criterias;

    public SerializableStandard() {
    }

    public SerializableStandard(Standard standard) {
        this.name = standard.getName();
        this.criterias = ListConverter.createList(standard.getCriterias(), this);
    }

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
    public int getPositiveAnswersCount() {
        return 0;
    }

    @Override
    public int setPositiveAnswersCount(int count) {
        return 0;
    }

    @Override
    public List<? extends Criteria> getCriterias() {
        return criterias;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public SerializableCriteria convert(Criteria input) {
        return new SerializableCriteria(input);
    }
}
