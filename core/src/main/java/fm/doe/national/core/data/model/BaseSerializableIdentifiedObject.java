package fm.doe.national.core.data.model;

public abstract class BaseSerializableIdentifiedObject implements IdentifiedObject {

    public static final long DEFAULT_ID = 0;

    @Override
    public long getId() {
        return DEFAULT_ID;
    }
}
