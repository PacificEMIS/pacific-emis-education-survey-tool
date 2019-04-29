package fm.doe.national.database_test.util;

import java.util.Date;

import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.PersistencePhotoEntity;
import fm.doe.national.data.persistence.entity.PersistenceStandard;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;

public class RoomTestData {

    public static PersistenceSurvey getSurveyFor_putSurveyEntityTest() {
        return new PersistenceSurvey("SCH001", "School1", new Date(10000));
    }

    public static PersistenceSurvey getSurveyFor_updateSurveyTest() {
        return new PersistenceSurvey("SCH002", "School2", new Date(10000));
    }

    public static PersistenceCategory getCategoryFor_createDeleteTest(long surveyId) {
        return new PersistenceCategory("PersistenceCategory One", surveyId);
    }

    public static PersistenceStandard getTestStandard(long categoryId) {
        return new PersistenceStandard("PersistenceStandard One", categoryId, "1");
    }

    public static PersistenceCriteria getTestCriteria(long standardId) {
        return new PersistenceCriteria("PersistenceCriteria One", standardId, "1");
    }

    public static PersistenceSubCriteria getTestSubCriteria(long criteriaId) {
        return new PersistenceSubCriteria("PersistenceSubCriteria One", criteriaId, "1");
    }

    public static PersistencePhotoEntity getTestPhotoEntity(long subCriteriaId) {
        return new PersistencePhotoEntity("https://avatars1.githubusercontent.com/u/28600571?s=200&v=4", subCriteriaId);
    }
}
