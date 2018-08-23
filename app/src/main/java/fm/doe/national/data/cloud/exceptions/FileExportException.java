package fm.doe.national.data.cloud.exceptions;

import android.support.annotation.NonNull;

public class FileExportException extends ReasonableException {
    @NonNull
    @Override
    protected String getMainMessage() {
        return "Unable to export file";
    }

    public FileExportException(String reason) {
        setReason(reason);
    }
}
