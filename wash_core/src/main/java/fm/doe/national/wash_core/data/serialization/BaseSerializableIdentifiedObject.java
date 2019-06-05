package fm.doe.national.wash_core.data.serialization;

import fm.doe.national.core.data.model.IdentifiedObject;

public abstract class BaseSerializableIdentifiedObject implements IdentifiedObject {

    private static final long DEFAULT_ID = 0;

    @Override
    public long getId() {
        return DEFAULT_ID;
    }
}
