package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.Photo;

@Root(name = "answer")
public class SerializableAnswer implements Answer {

    @Element
    AnswerState state;

    @Nullable
    @Element(required = false)
    String comment;

    public SerializableAnswer() {
    }

    public SerializableAnswer(@NonNull Answer otherAnswer) {
        this.state = otherAnswer.getState();
        this.comment = otherAnswer.getComment();
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
        return null;
    }
}
