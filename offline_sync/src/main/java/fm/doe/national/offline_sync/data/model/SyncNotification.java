package fm.doe.national.offline_sync.data.model;

public class SyncNotification {

    private Type type;
    private int value;

    public SyncNotification(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public SyncNotification(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public enum Type {
        DID_SEND_AVAILABLE_SURVEYS,
        DID_SEND_SURVEY,
        WILL_SEND_PHOTOS,
        DID_SEND_PHOTO,
        WILL_SAVE_PHOTOS,
        DID_SAVE_PHOTO,
        RECEIVE_BYTES,
        DID_FINISH_SYNC,
        DID_PUSH_SURVEY
    }
}
