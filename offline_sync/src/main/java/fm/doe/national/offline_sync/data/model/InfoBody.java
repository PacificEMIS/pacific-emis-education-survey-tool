package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;

public class InfoBody implements Serializable {

    long totalBytes;

    public InfoBody(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

}