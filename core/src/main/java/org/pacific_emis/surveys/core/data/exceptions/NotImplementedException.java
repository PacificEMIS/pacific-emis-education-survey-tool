package org.pacific_emis.surveys.core.data.exceptions;

public class NotImplementedException extends UnsupportedOperationException {

    private static final String MESSAGE = "Not Implemented";

    public NotImplementedException() {
        super(MESSAGE);
    }
}
