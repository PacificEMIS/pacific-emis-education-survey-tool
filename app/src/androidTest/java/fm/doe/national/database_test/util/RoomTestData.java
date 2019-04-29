package fm.doe.national.database_test.util;

import java.util.Date;

import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.PersistencePhoto;
import fm.doe.national.data.persistence.entity.PersistenceStandard;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;
import fm.doe.national.data.persistence.new_model.SurveyType;

public class RoomTestData {

    public static PersistenceSurvey getSurveyFor_putSurveyEntityTest() {
        return new PersistenceSurvey(1, SurveyType.SCHOOL_ACCREDITATION, null, new Date(10000));
    }

    public static PersistenceSurvey getSurveyFor_updateSurveyTest() {
        return new PersistenceSurvey(3, SurveyType.WASH, null, new Date(20000));
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

    public static PersistencePhoto getTestPhotoEntity(long subCriteriaId) {
        return new PersistencePhoto("https://avatars1.githubusercontent.com/u/28600571?s=200&v=4", subCriteriaId);
    }
}
