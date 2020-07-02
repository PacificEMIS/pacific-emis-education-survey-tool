package fm.doe.national.core.data.exceptions;

import android.content.Intent;

public class GmsUserRecoverableException extends RuntimeException {

    private final Intent intent;

    public GmsUserRecoverableException(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }
}
