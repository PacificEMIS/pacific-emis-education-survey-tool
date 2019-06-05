package fm.doe.national.wash_core.data.serialization.model;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.serialization.model.SerializablePhoto;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;

@Root(name = "answer")
public class SerializableAnswer implements Answer {

    @ElementList(entry = "item", required = false, inline = true)
    List<String> items;

    @Element(required = false)
    String comment;

    @Element(required = false)
    String inputText;

    @ElementList(required = false, inline = true)
    List<Variant> variants;

    @Element(name = "geo", required = false)
    Location location;

    @ElementList(required = false, inline = true)
    List<SerializablePhoto> photos;

    @Element(name = "b_answer", required = false)
    BinaryAnswerState binaryAnswerState;

    @Element(name = "t_answer", required = false)
    TernaryAnswerState ternaryAnswerState;

    public SerializableAnswer() {
        // required for serialization
    }

    public SerializableAnswer(Answer other) {
        this.items = other.getItems();
        this.comment = other.getComment();
        this.variants = other.getVariants();
        this.location = other.getLocation();
        this.binaryAnswerState = other.getBinaryAnswerState();
        this.ternaryAnswerState = other.getTernaryAnswerState();
        this.inputText = other.getInputText();

        if (other.getPhotos() != null) {
            this.photos = other.getPhotos().stream().map(SerializablePhoto::new).collect(Collectors.toList());
        }
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public List<SerializablePhoto> getPhotos() {
        return photos;
    }

    @Nullable
    @Override
    public BinaryAnswerState getBinaryAnswerState() {
        return binaryAnswerState;
    }

    @Nullable
    @Override
    public TernaryAnswerState getTernaryAnswerState() {
        return ternaryAnswerState;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Nullable
    @Override
    public String getInputText() {
        return inputText;
    }
}
