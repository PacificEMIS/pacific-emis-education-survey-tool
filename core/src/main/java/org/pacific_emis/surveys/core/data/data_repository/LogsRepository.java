package org.pacific_emis.surveys.core.data.data_repository;

import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LogsRepository {

    private final DataSource[] dataSources;

    public LogsRepository(DataSource... dataSources) {
        this.dataSources = dataSources;
    }

    public Single<List<Survey>> loadAllSurveys(AppRegion appRegion) {
        List<Single<List<Survey>>> singles = Arrays.stream(dataSources)
                .map(source -> source.loadAllSurveys(appRegion))
                .collect(Collectors.toList());
        return Single.merge(singles)
                .reduce(new ArrayList<>(), (list, item) -> {
                    list.addAll(item);
                    return list;
                });
    }

    public Completable deleteSurvey(Survey survey) throws UnsupportedOperationException {
        return dataSources[survey.getSurveyType().getValue()].deleteSurvey(survey.getId());
    }
}