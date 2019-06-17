package fm.doe.national.wash_core.data.serialization.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;

import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.Variant;
import fm.doe.national.wash_core.data.serialization.BaseSerializableIdentifiedObject;
import fm.doe.national.wash_core.data.serialization.converters.QuestionTypeConverter;

@Root(name = "question")
public class SerializableQuestion extends BaseSerializableIdentifiedObject implements Question {

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

    @Element(name = "answer", required = false)
    SerializableAnswer answer;

    public SerializableQuestion(Question other) {
        this.prefix = other.getPrefix();
        this.title = other.getTitle();
        this.type = other.getType();
        this.relation = other.getRelation();
        this.items = other.getItems();
        this.variants = other.getVariants();

        if (other.getAnswer() != null) {
            this.answer = new SerializableAnswer(other.getAnswer());
        }
    }

    public SerializableQuestion() {
        // required for serialization
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

    @Nullable
    @Override
    public SerializableAnswer getAnswer() {
        return answer;
    }

    @Nullable
    @Override
    public Relation getRelation() {
        return relation;
    }
}
