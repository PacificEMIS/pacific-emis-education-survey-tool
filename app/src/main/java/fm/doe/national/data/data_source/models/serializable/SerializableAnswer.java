package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SynchronizePlatform;

@Root(name = "answer")
public class SerializableAnswer implements Answer {

    @Element
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
