package fm.doe.national.wash_core.data.serialization.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.SubGroup;
import fm.doe.national.wash_core.data.serialization.BaseSerializableIdentifiedObject;

@Root(name = "subgroup")
public class SerializableSubGroup extends BaseSerializableIdentifiedObject implements SubGroup {

    @Element(name = "id")
    String prefix;

    @Element(name = "name")
    String title;

    @ElementList(inline = true)
    List<SerializableQuestion> questions;

    public SerializableSubGroup(SubGroup other) {
        this.prefix = other.getPrefix();
        this.title = other.getTitle();

        if (other.getQuestions() != null) {
            this.questions = other.getQuestions().stream().map(SerializableQuestion::new).collect(Collectors.toList());
        }
    }

    public SerializableSubGroup() {
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    @Override
    public List<? extends Question> getQuestions() {
        return questions;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new UnsupportedOperationException();
    }
}
