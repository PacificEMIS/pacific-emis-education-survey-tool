package fm.doe.national.data_source;

import java.util.List;

import fm.doe.national.models.survey.School;

public interface StaticDataSource {

    List<School> getSchoolList();

}
