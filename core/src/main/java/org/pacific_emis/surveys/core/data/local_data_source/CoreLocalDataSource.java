package org.pacific_emis.surveys.core.data.local_data_source;

import android.content.Context;

import androidx.room.Room;

import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.data.persistence.SchoolInfoDatabase;
import org.pacific_emis.surveys.core.data.persistence.dao.SchoolDao;
import org.pacific_emis.surveys.core.data.persistence.dao.SubjectDao;
import org.pacific_emis.surveys.core.data.persistence.dao.TeacherDao;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSchool;
import org.pacific_emis.surveys.core.data.persistence.model.RoomSubject;
import org.pacific_emis.surveys.core.data.persistence.model.RoomTeacher;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.pacific_emis.surveys.core.data.persistence.SchoolInfoDatabase.MIGRATION_1_2;

public abstract class CoreLocalDataSource implements DataSource {

    private static final String SCHOOLS_DATABASE_NAME = "schools.database";

    protected final SchoolInfoDatabase schoolInfoDatabase;

    protected final LocalSettings localSettings;

    protected final SchoolDao schoolDao;
    protected final TeacherDao teacherDao;
    protected final SubjectDao subjectDao;

    public CoreLocalDataSource(Context applicationContext, LocalSettings localSettings) {
        this.localSettings = localSettings;
        schoolInfoDatabase = Room.databaseBuilder(applicationContext, SchoolInfoDatabase.class, SCHOOLS_DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
        schoolDao = schoolInfoDatabase.getSchoolDao();
        teacherDao = schoolInfoDatabase.getTeacherDao();
        subjectDao = schoolInfoDatabase.getSubjectDao();
    }

    public void closeConnections() {
        schoolInfoDatabase.close();
    }

    @Override
    public Single<List<School>> loadSchools() {
        return Single.fromCallable(() -> schoolDao.getAll(localSettings.getAppRegion()))
                .map(ArrayList::new);
    }

    @Override
    public Single<List<Teacher>> loadTeachers() {
        return Single.fromCallable(() -> teacherDao.getAll(localSettings.getAppRegion()))
                .map(ArrayList::new);
    }

    @Override
    public Single<List<Subject>> loadSubjects() {
        return Single.fromCallable(() -> subjectDao.getAll(localSettings.getAppRegion()))
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

}
