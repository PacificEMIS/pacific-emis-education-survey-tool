package org.pacific_emis.surveys.core.data.model.abstract_implementations;

import org.pacific_emis.surveys.core.data.model.Subject;

public abstract class SubjectImpl implements Subject {
        @Override
        public String toString() {
            return getName();
        }
}
