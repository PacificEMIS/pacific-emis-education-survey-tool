package fm.doe.national.data.cloud.uploader;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;

public class UploadingData {
    public String content;
    public String filename;

    public UploadingData(String content, String filename) {
        this.content = content;
        this.filename = filename;
    }

    public UploadingData(String content, SchoolAccreditationPassing passing) {
        this.content = content;
        this.filename = createFilename(passing);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UploadingData && filename.equals(((UploadingData) obj).filename);
    }

    @NonNull
    public static String createFilename(SchoolAccreditationPassing passing) {
        School school = passing.getSchool();
        DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
        return new StringBuilder()
                .append(school.getName())
                .append('-')
                .append(school.getId())
                .append('-')
                .append(dateFormat.format(passing.getStartDate()))
                .toString();
    }
}
