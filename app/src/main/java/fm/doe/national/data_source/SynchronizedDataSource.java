package fm.doe.national.data_source;

import java.util.List;

import fm.doe.national.data_source.db.DbAccreditationDataSource;
import fm.doe.national.data_source.static_source.StaticDataSource;
import fm.doe.national.models.survey.Answer;
import fm.doe.national.models.survey.Criteria;
import fm.doe.national.models.survey.GroupStandard;
import fm.doe.national.models.survey.School;
import fm.doe.national.models.survey.Standard;
import fm.doe.national.models.survey.SubCriteria;
import fm.doe.national.models.survey.Survey;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class SynchronizedDataSource implements DataSource {

    private final StaticDataSource staticDataSource;
    private final DbAccreditationDataSource accreditationDataSource;

    public SynchronizedDataSource(StaticDataSource staticDataSource,
                                  DbAccreditationDataSource dataSource) {
        this.staticDataSource = staticDataSource;
        this.accreditationDataSource = dataSource;
    }
    
    
    @Override
    public Single<School> createSchool(String name) {
        return null;
    }

    @Override
    public Single<List<School>> requestSchools() {
        return accreditationDataSource.requestSchools()
                .flatMap(new Function<List<School>, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(List<School> schools) throws Exception {
                        if (schools.isEmpty()) {

                            staticDataSource.requestSchools()
                                    .flatMap(new Function<List<School>, SingleSource<?>>() {
                                        @Override
                                        public SingleSource<?> apply(List<School> schools) throws Exception {
                                            accreditationDataSource.crea
                                            return null;
                                        }
                                    })


                        } else {
                            return Single.just(schools);
                        }
                    }
                });
    }

    @Override
    public Single<Survey> createSurvey(School school, int year) {
        return null;
    }

    @Override
    public Single<GroupStandard> createGroupStandard() {
        return null;
    }

    @Override
    public Single<List<GroupStandard>> requestGroupStandard() {
        return null;
    }

    @Override
    public Single<Standard> createStandard(String name, GroupStandard group) {
        return null;
    }

    @Override
    public Single<Criteria> createCriteria(String name, Standard standard) {
        return null;
    }

    @Override
    public Single<SubCriteria> createSubCriteria(String name, String question, Criteria criteria) {
        return null;
    }

    @Override
    public Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey) {
        return null;
    }
}
