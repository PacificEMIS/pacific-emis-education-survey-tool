package org.pacific_emis.surveys.core.data.model.abstract_implementations;

import org.pacific_emis.surveys.core.data.model.Teacher;

public abstract class TeacherImpl implements Teacher {
    @Override
    public String toString() {
        return getName();
    }
}
