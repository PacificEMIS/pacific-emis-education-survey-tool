package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SynchronizePlatform;

@Root(name = "answer")
public class SerializableAnswer implements Answer {

    @Element
    State state;

    @Nullable
    @Element(required = false)
    String comment;

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

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(@NonNull String comment) {
        this.comment = comment;
    }

    @Override
    public List<String> getPhotosPaths() {
        return new ArrayList<>();
    }

    @Override
    public void setPhotoPath(List<String> paths) {
        // nothing
    }
}
