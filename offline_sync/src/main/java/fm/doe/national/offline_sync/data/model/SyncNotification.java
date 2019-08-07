package fm.doe.national.offline_sync.data.model;

public class SyncNotification {

    private Type type;
    private int value;
    private Throwable throwable;

    public static SyncNotification error(Throwable throwable) {
        return new SyncNotification(Type.ERROR, 0, throwable);
    }

    public static SyncNotification just(Type type) {
        return new SyncNotification(type, 0, null);
    }

    public static SyncNotification withValue(Type type, int value) {
        return new SyncNotification(type, value, null);
    }

    private SyncNotification(Type type, int value, Throwable throwable) {
        this.type = type;
        this.value = value;
        this.throwable = throwable;
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public enum Type {
        DID_SEND_AVAILABLE_SURVEYS,
        DID_SEND_SURVEY,
        WILL_SEND_PHOTOS,
        DID_SEND_PHOTO,
        WILL_SAVE_PHOTOS,
        DID_SAVE_PHOTO,
        DID_FINISH_SYNC,
        ERROR
    }
}
