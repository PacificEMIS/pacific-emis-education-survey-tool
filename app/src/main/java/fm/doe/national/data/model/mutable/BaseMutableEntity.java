package fm.doe.national.data.model.mutable;

import java.io.Serializable;

import fm.doe.national.data.model.IdentifiedObject;

public class BaseMutableEntity implements Serializable, IdentifiedObject {

    private long id;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
