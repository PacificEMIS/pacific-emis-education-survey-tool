package fm.doe.national.database_test;

import java.util.Date;

import fm.doe.national.data.persistence.entity.Survey;

class RoomTestData {

    static Survey getSurveyFor_putSurveyEntityTest() {
        return new Survey("SCH001", "School1", new Date(10000));
    }

    static Survey getSurveyFor_updateSurveyTest() {
        return new Survey("SCH002", "School2", new Date(10000));
    }
}
