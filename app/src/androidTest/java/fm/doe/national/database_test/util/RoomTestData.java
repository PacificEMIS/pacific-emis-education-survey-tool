package fm.doe.national.database_test.util;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.EvaluationForm;
import fm.doe.national.data.model.SurveyType;
import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.entity.RoomAnswer;
import fm.doe.national.data.persistence.entity.RoomCategory;
import fm.doe.national.data.persistence.entity.RoomCriteria;
import fm.doe.national.data.persistence.entity.RoomPhoto;
import fm.doe.national.data.persistence.entity.RoomSchool;
import fm.doe.national.data.persistence.entity.RoomStandard;
import fm.doe.national.data.persistence.entity.RoomSubCriteria;
import fm.doe.national.data.persistence.entity.RoomSurvey;

public class RoomTestData {

    @NotNull
    public static AppDatabase createAppDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        return Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public static RoomSurvey getSurveyFor_putSurveyEntityTest() {
        return new RoomSurvey(1, SurveyType.SCHOOL_ACCREDITATION, null, null, new Date(10000));
    }

    public static RoomSurvey getSurveyFor_updateSurveyTest() {
        return new RoomSurvey(3, SurveyType.WASH, null, null, new Date(20000));
    }

    public static RoomCategory getCategoryFor_createDeleteTest(long surveyId) {
        return new RoomCategory("RoomCategory One", surveyId, EvaluationForm.SCHOOL_EVALUATION);
    }

    public static RoomStandard getTestStandard(long categoryId) {
        return new RoomStandard("RoomStandard One", categoryId, "1");
    }

    public static RoomCriteria getTestCriteria(long standardId) {
        return new RoomCriteria("RoomCriteria One", standardId, "1");
    }

    public static RoomSubCriteria getTestSubCriteria(long criteriaId) {
        return new RoomSubCriteria("RoomSubCriteria One", criteriaId, "1");
    }

    public static RoomPhoto getTestPhotoEntity(long answerId) {
        RoomPhoto photo = new RoomPhoto(answerId);
        photo.remoteUrl = "https://avatars1.githubusercontent.com/u/28600571?s=200&v=4";
        return photo;
    }

    public static RoomAnswer getTestAnswer(long subCriteriaId) {
        RoomAnswer answer = new RoomAnswer(subCriteriaId);
        answer.state = AnswerState.POSITIVE;
        answer.comment = "Comment";
        return answer;
    }

    public static RoomSchool getTestSchool() {
        return new RoomSchool("Test School 1", "SCH001");
    }
}
