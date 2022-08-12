package org.pacific_emis.surveys.core.data.data_repository;

import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.model.SurveyLog;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.LogAction;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class DataRepository implements DataSource {

    private final DataSource[] dataSources;

    public DataRepository(DataSource... dataSources) {
        this.dataSources = dataSources;
    }

    @Override
    public Single<Result<List<School>>> loadSchools(AppRegion appRegion) {
        return loadSchools(0, null, appRegion);
    }

    private Single<Result<List<School>>> loadSchools(int defaultDataSourceNumber,
                                                     @NonNull Throwable prevError,
                                                     AppRegion appRegion) {
        if (defaultDataSourceNumber < dataSources.length) {
            final int nextDataSourceNumber = defaultDataSourceNumber + 1;
            return dataSources[defaultDataSourceNumber]
                            .loadSchools(appRegion)
                            .onErrorResumeNext(error -> loadSchools(nextDataSourceNumber, error, appRegion))
                            .map(item -> {
                                if (item.getError() == null) {
                                    return new Result<>(item.getData(), prevError);
                                }
                                return item;
                            });
        }
        return null;
    }

    @Override
    public Single<List<Teacher>> loadTeachers(AppRegion appRegion) { return dataSources[0].loadTeachers(appRegion); }

    @Override
    public Single<List<Subject>> loadSubjects(AppRegion appRegion) { return dataSources[0].loadSubjects(appRegion); }

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
    public Single<Survey> getTemplateSurvey(AppRegion appRegion) {
        Single<Survey> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].getTemplateSurvey(appRegion);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<Survey> loadSurvey(AppRegion appRegion, long surveyId) {
        Single<Survey> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].loadSurvey(appRegion, surveyId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<List<Survey>> loadAllSurveys(AppRegion appRegion) {
        Single<List<Survey>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].loadAllSurveys(appRegion);
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
    public Single<Survey> createSurvey(
            String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail, AppRegion appRegion, String tabletId
    ) {
        Single<Survey> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].createSurvey(schoolId, schoolName, createDate, surveyTag, userEmail, appRegion, tabletId);
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
    public Completable createPartiallySavedSurvey(AppRegion appRegion, Survey survey) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].createPartiallySavedSurvey(appRegion, survey);
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

    @Override
    public void setSurveyUploadState(Survey survey, UploadState uploadState) {
        for (int i = 0; i < dataSources.length; ) {
            try {
                dataSources[i].setSurveyUploadState(survey, uploadState);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
    }

    @Override
    public void setSurveyDriveFileId(Survey survey, String driveFileId) {
        for (int i = 0; i < dataSources.length; ) {
            try {
                dataSources[i].setSurveyDriveFileId(survey, driveFileId);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
    }

    @Override
    public Completable saveLogInfo(Survey survey, LogAction action) {
        Completable result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].saveLogInfo(survey, action);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }

    @Override
    public Single<List<SurveyLog>> loadLogs(AppRegion appRegion) {
        Single<List<SurveyLog>> result = null;
        for (int i = 0; i < dataSources.length; ) {
            try {
                result = dataSources[i].loadLogs(appRegion);
                break;
            } catch (UnsupportedOperationException e) {
                i++;
            }
        }
        return result;
    }
}
