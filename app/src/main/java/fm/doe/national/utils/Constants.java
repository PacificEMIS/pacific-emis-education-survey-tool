package fm.doe.national.utils;

public class Constants {
    public static final String FILE_MIME_TYPE = "text/plain";
    public static final String PREF_KEY_LOGO_PATH = "PREF_KEY_LOGO_PATH";
    public static final String AUTHORITY_FILE_PROVIDER = "fm.doe.national.fileprovider";
    public static final int SIZE_THUMB_PICTURE = 200;

    public class Errors {
        public static final String EXPORT_FOLDER_NOT_SPECIFIED = "Export folder not specified";
        public static final String DRIVE_RESOURCE_CLIENT_IS_NULL = "DriveResourceClient is null";
        public static final String DRIVE_CLIENT_IS_NULL = "DriveClient is null";
        public static final String NO_ACTIVITIES = "No activities is currently running";
        public static final String AUTH_FAILED = "Authentication failure";
        public static final String AUTH_DECLINED = "Authentication declined";
        public static final String WRONG_INTENT = "Activity started with wrong intent";
        public static final String PICKER_DECLINED = "Picker declined";
        public static final String NOT_AUTHORIZED = "User not authorized";
        public static final String WRONG_FRAGMENT_ARGS = "Fragment should be created with static create() method";
        public static final String WRONT_SUMMARY_INPUT_PARAMETER = "Progresses must be less than 4";
        public static final String SUMMARY_COLUMNS_MISMATCH = "Columns mismatch";
    }
}
