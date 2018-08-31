package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SynchronizePlatform;
import fm.doe.national.utils.converters.XmlStateConverter;

@Xml(name = "answer")
public class SerializableAnswer implements Answer {

    @PropertyElement(converter = XmlStateConverter.class)
    State state;

    public SerializableAnswer() {
    }

    public SerializableAnswer(Answer answer) {
        this.state = answer.getState();
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Nullable
    @Override
    public List<SynchronizePlatform> getSynchronizedPlatforms() {
        return null;
    }

    @Override
    public void addSynchronizedPlatform(SynchronizePlatform platform) {
        // nothing
    }
}
