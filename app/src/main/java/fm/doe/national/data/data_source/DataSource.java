package fm.doe.national.data.data_source;


import java.util.List;

import fm.doe.national.data.data_source.db.models.survey.OrmLiteAnswer;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSchool;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSubCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSurvey;
import io.reactivex.Single;

public interface DataSource {

    Single<OrmLiteSchool> createSchool(String name);

    Single<List<OrmLiteSchool>> requestSchools();

    Single<OrmLiteSurvey> createSurvey(OrmLiteSchool school, int year);

    Single<OrmLiteGroupStandard> createGroupStandard();

    Single<List<OrmLiteGroupStandard>> requestGroupStandard();

    Single<OrmLiteStandard> createStandard(String name, OrmLiteGroupStandard group);

    Single<OrmLiteCriteria> createCriteria(String name, OrmLiteStandard standard);

    Single<OrmLiteSubCriteria> createSubCriteria(String name, OrmLiteCriteria criteria);

    Single<OrmLiteAnswer> createAnswer(boolean answer, OrmLiteSubCriteria criteria, OrmLiteSurvey survey);

}
