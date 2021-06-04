package org.pacific_emis.surveys.offline_sync.data.model;

import java.io.Serializable;

public class BtMessage implements Serializable {

    private Type type;
    private String content;

    public BtMessage(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public enum Type {
        REQUEST_SURVEYS, REQUEST_FILLED_SURVEY, RESPONSE_SURVEYS, RESPONSE_FILLED_SURVEY, REQUEST_PHOTO, END
    }
}
