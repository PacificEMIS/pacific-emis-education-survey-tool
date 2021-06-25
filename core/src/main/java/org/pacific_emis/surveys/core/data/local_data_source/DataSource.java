package org.pacific_emis.surveys.core.data.local_data_source;

import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<List<School>> loadSchools() throws UnsupportedOperationException;

    Single<List<Teacher>> loadTeachers() throws UnsupportedOperationException;

    Single<List<Subject>> loadSubjects() throws UnsupportedOperationException;

    Completable rewriteAllSchools(List<School> schools) throws UnsupportedOperationException;

    Completable rewriteAllTeachers(List<Teacher> teachers) throws UnsupportedOperationException;

    Completable rewriteAllSubjects(List<Subject> subjects) throws UnsupportedOperationException;

    Completable rewriteTemplateSurvey(Survey survey) throws UnsupportedOperationException;

    Single<Survey> getTemplateSurvey() throws UnsupportedOperationException;

    Single<Survey> loadSurvey(long surveyId) throws UnsupportedOperationException;

    Single<List<Survey>> loadAllSurveys() throws UnsupportedOperationException;

    Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag) throws UnsupportedOperationException;

    Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail) throws UnsupportedOperationException;

    Completable deleteSurvey(long surveyId) throws UnsupportedOperationException;

    Single<Photo> createPhoto(Photo photo, long answerId) throws UnsupportedOperationException;

    Completable deletePhoto(long photoId) throws UnsupportedOperationException;

    Completable deleteCreatedSurveys() throws UnsupportedOperationException;

    Completable createPartiallySavedSurvey(Survey survey) throws UnsupportedOperationException;

    void updateSurvey(Survey survey) throws UnsupportedOperationException;

    List<Photo> getPhotos(Survey survey) throws UnsupportedOperationException;

    Completable updatePhotoWithRemote(Photo photo, String remoteFileId) throws UnsupportedOperationException;

//    Single<List<School>> loadSchools();
//
//    Single<List<Teacher>> loadTeachers();
//
//    Single<List<Subject>> loadSubjects();
//
//    Completable rewriteAllSchools(List<School> schools);
//
//    Completable rewriteAllTeachers(List<Teacher> teachers);
//
//    Completable rewriteAllSubjects(List<Subject> subjects);
//
//    Completable rewriteTemplateSurvey(Survey survey);
//
//    Single<Survey> getTemplateSurvey();
//
//    Single<Survey> loadSurvey(long surveyId);
//
//    Single<List<Survey>> loadAllSurveys();
//
//    Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag);
//
//    Single<Survey> createSurvey(String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail);
//
//    Completable deleteSurvey(long surveyId);
//
//    Single<Photo> createPhoto(Photo photo, long answerId);
//
//    Completable deletePhoto(long photoId);
//
//    Completable deleteCreatedSurveys();
//
//    Completable createPartiallySavedSurvey(Survey survey);
//
//    void updateSurvey(Survey survey);
//
//    List<Photo> getPhotos(Survey survey);
//
//    Completable updatePhotoWithRemote(Photo photo, String remoteFileId);
}
