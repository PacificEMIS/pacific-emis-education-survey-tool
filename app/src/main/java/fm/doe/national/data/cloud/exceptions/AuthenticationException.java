package fm.doe.national.data.cloud.exceptions;


import androidx.annotation.NonNull;

public class AuthenticationException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Authentication failed";
    }

    public AuthenticationException(String reason) {
        setReason(reason);
    }
}
