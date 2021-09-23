package org.pacific_emis.surveys.core.data.local_data_source;

import org.pacific_emis.surveys.core.data.data_repository.Result;
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

public interface DataSource {

    Single<Result<List<School>>> loadSchools(AppRegion appRegion) throws UnsupportedOperationException;

    Single<List<Teacher>> loadTeachers(AppRegion appRegion) throws UnsupportedOperationException;

    Single<List<Subject>> loadSubjects(AppRegion appRegion) throws UnsupportedOperationException;

    Completable rewriteAllSchools(List<School> schools) throws UnsupportedOperationException;

    Completable rewriteAllTeachers(List<Teacher> teachers) throws UnsupportedOperationException;

    Completable rewriteAllSubjects(List<Subject> subjects) throws UnsupportedOperationException;

    Completable rewriteTemplateSurvey(Survey survey) throws UnsupportedOperationException;

    Single<Survey> getTemplateSurvey(AppRegion appRegion) throws UnsupportedOperationException;

    Single<Survey> loadSurvey(AppRegion appRegion, long surveyId) throws UnsupportedOperationException;

    Single<List<Survey>> loadAllSurveys(AppRegion appRegion) throws UnsupportedOperationException;

    Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag) throws UnsupportedOperationException;

    Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail, AppRegion appRegion) throws UnsupportedOperationException;

    Completable deleteSurvey(long surveyId) throws UnsupportedOperationException;

    Single<Photo> createPhoto(Photo photo, long answerId) throws UnsupportedOperationException;

    Completable deletePhoto(long photoId) throws UnsupportedOperationException;

    Completable deleteCreatedSurveys() throws UnsupportedOperationException;

    Completable createPartiallySavedSurvey(AppRegion appRegion, Survey survey) throws UnsupportedOperationException;

    void updateSurvey(Survey survey) throws UnsupportedOperationException;

    List<Photo> getPhotos(Survey survey) throws UnsupportedOperationException;

    Completable updatePhotoWithRemote(Photo photo, String remoteFileId) throws UnsupportedOperationException;
}
