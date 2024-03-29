package org.pacific_emis.surveys.core.data.model.mutable;

import java.io.Serializable;
import java.util.Objects;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;

public class BaseMutableEntity implements Serializable, IdentifiedObject {

    protected long id;

    public BaseMutableEntity() {
        // nothing
    }

    public BaseMutableEntity(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMutableEntity that = (BaseMutableEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
