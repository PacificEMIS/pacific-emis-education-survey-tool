package fm.doe.national.remote_settings.model;

import java.io.Serializable;

public abstract class ForceableObject<T> implements Serializable {

    private boolean force;
    private T value;

    public boolean isForce() {
        return force;
    }

    public T getValue() {
        return value;
    }
}
