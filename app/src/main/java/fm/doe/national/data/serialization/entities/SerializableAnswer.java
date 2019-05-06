package fm.doe.national.data.serialization.entities;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.AnswerState;

@Root(name = "answer")
public class SerializableAnswer implements Answer, Serializable {

    @Element
    AnswerState state;

    @Nullable
    @Element(required = false)
    String comment;

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

}
