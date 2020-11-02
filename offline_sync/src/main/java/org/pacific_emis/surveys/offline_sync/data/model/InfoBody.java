package org.pacific_emis.surveys.offline_sync.data.model;

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