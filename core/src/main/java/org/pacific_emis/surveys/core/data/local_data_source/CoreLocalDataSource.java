package org.pacific_emis.surveys.core.data.local_data_source;

import android.content.Context;

import androidx.room.Room;

import org.pacific_emis.surveys.core.data.data_repository.Result;
import org.pacific_emis.surveys.core.data.model.SurveyLog;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.data.persistence.SchoolInfoDatabase;
import org.pacific_emis.surveys.core.data.persistence.dao.SurveyLogsDao;
import org.pacific_emis.surveys.core.data.persistence.dao.SchoolDao;
import org.pacific_emis.surveys.core.data.persistence.dao.SubjectDao;
import org.pacific_emis.surveys.core.data.persistence.dao.TeacherDao;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSurveyLog;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSchool;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSubject;
import org.pacific_emis.surveys.core.data.persistence.model.RoomTeacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.LogAction;
import org.pacific_emis.surveys.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.pacific_emis.surveys.core.data.persistence.SchoolInfoDatabase.MIGRATION_1_2;
import static org.pacific_emis.surveys.core.data.persistence.SchoolInfoDatabase.MIGRATION_2_3;

public abstract class CoreLocalDataSource implements DataSource {

    private static final String SCHOOLS_DATABASE_NAME = "schools.database";

    protected final SchoolInfoDatabase schoolInfoDatabase;

    protected final SchoolDao schoolDao;
    protected final TeacherDao teacherDao;
    protected final SubjectDao subjectDao;
    protected final SurveyLogsDao surveyLogsDao;

    public CoreLocalDataSource(Context applicationContext) {
        schoolInfoDatabase = Room.databaseBuilder(applicationContext, SchoolInfoDatabase.class, SCHOOLS_DATABASE_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();
        schoolDao = schoolInfoDatabase.getSchoolDao();
        teacherDao = schoolInfoDatabase.getTeacherDao();
        subjectDao = schoolInfoDatabase.getSubjectDao();
        surveyLogsDao = schoolInfoDatabase.getSurveyLogsDao();
    }

    public void closeConnections() {
        schoolInfoDatabase.close();
    }

    @Override
    public Single<Result<List<School>>> loadSchools(AppRegion appRegion) {
        return Single.fromCallable(() -> schoolDao.getAll(appRegion))
                .map(item -> new Result<>(new ArrayList<>(item), null));
    }

    @Override
    public Single<List<Teacher>> loadTeachers(AppRegion appRegion) {
        return Single.fromCallable(() -> teacherDao.getAll(appRegion))
                .map(ArrayList::new);
    }

    @Override
    public Single<List<Subject>> loadSubjects(AppRegion appRegion) {
        return Single.fromCallable(() -> subjectDao.getAll(appRegion))
                .map(ArrayList::new);
    }

    @Override
    public Completable rewriteAllSchools(List<School> schools) {
        if (CollectionUtils.isEmpty(schools)) {
            return Completable.complete();
        }

        final AppRegion appRegion = schools.get(0).getAppRegion();

        return Observable.fromIterable(schools)
                .map(RoomSchool::new)
                .toList()
                .flatMapCompletable(roomSchools -> Completable.fromAction(() -> {
                    schoolDao.deleteAllForAppRegion(appRegion);
                    schoolDao.insert(roomSchools);
                }));
    }

    @Override
    public Completable rewriteAllTeachers(List<Teacher> teachers) {
        if (CollectionUtils.isEmpty(teachers)) {
            return Completable.complete();
        }

        final AppRegion appRegion = teachers.get(0).getAppRegion();

        return Observable.fromIterable(teachers)
                .map(RoomTeacher::new)
                .toList()
                .flatMapCompletable(roomTeachers -> Completable.fromAction(() -> {
                    teacherDao.deleteAllForAppRegion(appRegion);
                    teacherDao.insert(roomTeachers);
                }));
    }
    
    @Override
    public Completable rewriteAllSubjects(List<Subject> subjects) {
        if (CollectionUtils.isEmpty(subjects)) {
            return Completable.complete();
        }

        final AppRegion appRegion = subjects.get(0).getAppRegion();

        return Observable.fromIterable(subjects)
                .map(RoomSubject::new)
                .toList()
                .flatMapCompletable(roomSubjects -> Completable.fromAction(() -> {
                    subjectDao.deleteAllForAppRegion(appRegion);
                    subjectDao.insert(roomSubjects);
                }));
    }

    @Override
    public Completable saveLogInfo(Survey survey, LogAction action) {
        return Completable.fromAction(() -> {
            RoomSurveyLog deletedSurvey = new RoomSurveyLog(survey, action);
            surveyLogsDao.insert(deletedSurvey);
        });
    }

    @Override
    public Single<List<SurveyLog>> loadLogs(AppRegion appRegion) {
        return Single.fromCallable(() -> surveyLogsDao.getAll(appRegion)).map(ArrayList::new);
    }
}
