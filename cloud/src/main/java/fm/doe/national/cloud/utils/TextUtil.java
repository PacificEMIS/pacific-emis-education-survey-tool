package fm.doe.national.cloud.utils;

import androidx.annotation.NonNull;

import java.util.Date;

import fm.doe.national.core.data.model.School;
import fm.doe.national.core.utils.DateUtils;

public class TextUtil {

    @NonNull
    public static String createSurveyFileName(School school, Date date) {
        return school.getName() + '-' + school.getId() + '-' + DateUtils.format(date) + ".xml";
    }

}
