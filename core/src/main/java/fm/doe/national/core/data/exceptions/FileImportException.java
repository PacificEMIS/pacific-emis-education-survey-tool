package fm.doe.national.core.data.exceptions;


import androidx.annotation.NonNull;

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
