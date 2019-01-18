package fm.doe.national.database_test.util;

import java.util.Date;

import fm.doe.national.data.persistence.entity.Category;
import fm.doe.national.data.persistence.entity.Criteria;
import fm.doe.national.data.persistence.entity.PhotoEntity;
import fm.doe.national.data.persistence.entity.Standard;
import fm.doe.national.data.persistence.entity.SubCriteria;
import fm.doe.national.data.persistence.entity.Survey;

public class RoomTestData {

    public static Survey getSurveyFor_putSurveyEntityTest() {
        return new Survey("SCH001", "School1", new Date(10000));
    }

    public static Survey getSurveyFor_updateSurveyTest() {
        return new Survey("SCH002", "School2", new Date(10000));
    }

    public static Category getCategoryFor_createDeleteTest(long surveyId) {
        return new Category("Category One", surveyId);
    }

    public static Standard getTestStandard(long categoryId) {
        return new Standard("Standard One", categoryId, "1");
    }

    public static Criteria getTestCriteria(long standardId) {
        return new Criteria("Criteria One", standardId, "1");
    }

    public static SubCriteria getTestSubCriteria(long criteriaId) {
        return new SubCriteria("SubCriteria One", criteriaId, "1");
    }

    public static PhotoEntity getTestPhotoEntity(long subCriteriaId) {
        return new PhotoEntity("https://avatars1.githubusercontent.com/u/28600571?s=200&v=4", subCriteriaId);
    }
}
