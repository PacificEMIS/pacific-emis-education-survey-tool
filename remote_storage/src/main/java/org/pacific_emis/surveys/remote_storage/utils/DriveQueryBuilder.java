package org.pacific_emis.surveys.remote_storage.utils;

public class DriveQueryBuilder {

    private StringBuilder stringBuilder = new StringBuilder();

    public DriveQueryBuilder parentId(String parentId) {
        addAndIfNeed().append("'")
                .append(parentId)
                .append("' in parents ");

        return this;
    }

    public DriveQueryBuilder mimeType(String mimeType) {
        addAndIfNeed().append("mimeType = '")
                .append(mimeType)
                .append("' ");
        return this;
    }

    public DriveQueryBuilder name(String name) {
        addAndIfNeed().append("name = '")
                .append(name)
                .append("' ");
        return this;
    }

    private StringBuilder addAndIfNeed() {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("and ");
        }

        return stringBuilder;
    }

    public String build() {
        return stringBuilder.toString();
    }
}
