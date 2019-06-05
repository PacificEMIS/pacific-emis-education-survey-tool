package fm.doe.national.wash_core.data.serialization.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.serialization.converters.QuestionTypeConverter;

@Root(name = "question")
public class SerializableQuestion implements Question {

    @Element(name = "id")
    String prefix;

    @Element(name = "name")
    String title;

    @Element(name = "flags")
    @Convert(QuestionTypeConverter.class)
    QuestionType type;

    @Element(name = "relation", required = false)
    Relation relation;

    @ElementList(entry = "item", inline = true, required = false)
    List<String> items;

    @ElementList(inline = true, required = false)
    List<Variant> variants;

    public SerializableQuestion(Question other) {
        this.prefix = other.getPrefix();
        this.title = other.getTitle();
        this.type = other.getType();
    }

    public SerializableQuestion() {
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

    @NonNull
    @Override
    public QuestionType getType() {
        return type;
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return MutableProgress.createEmptyProgress();
    }
}
