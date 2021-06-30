package org.pacific_emis.surveys.core.data.data_repository;

import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class DataRepository implements DataSource {

    private final DataSource[] dataSources;

    public DataRepository(DataSource... dataSources) {
        this.dataSources = dataSources;
    }

    @Override
    public Single<List<School>> loadSchools() {
        Single<List<School>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = getSchoolsFromDataSource(i);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    private Single<List<School>> getSchoolsFromDataSource(int dataSourceNumber) throws UnsupportedOperationException {
        if (dataSourceNumber < dataSources.length - 1)
            return dataSources[dataSourceNumber].loadSchools().onErrorResumeNext(error -> getSchoolsFromDataSource(dataSourceNumber + 1));
        else
            return dataSources[dataSourceNumber].loadSchools();
    }

    @Override
    public Single<List<Teacher>> loadTeachers() {
        Single<List<Teacher>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = getTeachersFromDataSource(i);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    private Single<List<Teacher>> getTeachersFromDataSource(int dataSourceNumber) throws UnsupportedOperationException {
        if (dataSourceNumber < dataSources.length - 1)
            return dataSources[dataSourceNumber].loadTeachers().onErrorResumeNext(error -> getTeachersFromDataSource(dataSourceNumber + 1));
        else
            return dataSources[dataSourceNumber].loadTeachers();
    }

    @Override
    public Single<List<Subject>> loadSubjects() {
        Single<List<Subject>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = getSubjectsFromDataSource(i);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }
    
    private Single<List<Subject>> getSubjectsFromDataSource(int dataSourceNumber) throws UnsupportedOperationException {
        if (dataSourceNumber < dataSources.length - 1)
            return dataSources[dataSourceNumber].loadSubjects().onErrorResumeNext(error -> getSubjectsFromDataSource(dataSourceNumber + 1));
        else
            return dataSources[dataSourceNumber].loadSubjects();
    }

    @Override
    public Completable rewriteAllSchools(List<School> schools) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].rewriteAllSchools(schools);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable rewriteAllTeachers(List<Teacher> teachers) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].rewriteAllTeachers(teachers);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable rewriteAllSubjects(List<Subject> subjects) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].rewriteAllSubjects(subjects);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable rewriteTemplateSurvey(Survey survey) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].rewriteTemplateSurvey(survey);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<Survey> getTemplateSurvey() {
        Single<Survey> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].getTemplateSurvey();
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<Survey> loadSurvey(long surveyId) {
        Single<Survey> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].loadSurvey(surveyId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<List<Survey>> loadAllSurveys() {
        Single<List<Survey>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].loadAllSurveys();
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag) {
        Single<List<Survey>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].loadSurveys(schoolId, appRegion, surveyTag);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail) {
        Single<Survey> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].createSurvey(schoolId, schoolName, createDate, surveyTag, userEmail);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable deleteSurvey(long surveyId) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].deleteSurvey(surveyId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<Photo> createPhoto(Photo photo, long answerId) {
        Single<Photo> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].createPhoto(photo, answerId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable deletePhoto(long photoId) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].deletePhoto(photoId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable deleteCreatedSurveys() {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].deleteCreatedSurveys();
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable createPartiallySavedSurvey(Survey survey) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].createPartiallySavedSurvey(survey);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public void updateSurvey(Survey survey) {
        for (int i = 0; i < dataSources.length; ) {
            try {
                dataSources[i].updateSurvey(survey);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
    }

    @Override
    public List<Photo> getPhotos(Survey survey) {
        List<Photo> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].getPhotos(survey);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Completable updatePhotoWithRemote(Photo photo, String remoteFileId) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].updatePhotoWithRemote(photo, remoteFileId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }
}
