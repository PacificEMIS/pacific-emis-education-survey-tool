package fm.doe.national.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.serialization.model.SerializablePhoto;

@Root(name = "answer")
public class SerializableAnswer implements Answer {

    @Element
    AnswerState state;

    @Nullable
    @Element(required = false)
    String comment;

    @Nullable
    @ElementList(entry = "photo", required = false, inline = true)
    List<SerializablePhoto> photos;

    public SerializableAnswer() {
    }

    public SerializableAnswer(@NonNull Answer otherAnswer) {
        this.state = otherAnswer.getState();
        this.comment = otherAnswer.getComment();

        if (otherAnswer.getPhotos() != null) {
            this.photos = otherAnswer.getPhotos().stream().map(SerializablePhoto::new).collect(Collectors.toList());
        }
    }

    @Override
    public AnswerState getState() {
        return state;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Nullable
    @Override
    public List<? extends Photo> getPhotos() {
        return photos;
    }
}
