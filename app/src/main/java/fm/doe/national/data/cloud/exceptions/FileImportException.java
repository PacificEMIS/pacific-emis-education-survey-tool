package fm.doe.national.data.cloud.exceptions;

import android.support.annotation.NonNull;

public class FileImportException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Failed to import file";
    }

    public FileImportException(String reason) {
        setReason(reason);
    }
}