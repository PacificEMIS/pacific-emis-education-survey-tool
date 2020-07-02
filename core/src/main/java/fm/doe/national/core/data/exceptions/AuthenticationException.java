package fm.doe.national.core.data.exceptions;


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
