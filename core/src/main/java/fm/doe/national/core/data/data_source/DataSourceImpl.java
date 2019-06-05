package fm.doe.national.core.data.data_source;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.core.BuildConfig;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.persistence.SchoolsDatabase;
import fm.doe.national.core.data.persistence.dao.SchoolDao;
import fm.doe.national.core.data.persistence.model.RoomSchool;
import fm.doe.national.core.preferences.GlobalPreferences;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public abstract class DataSourceImpl implements DataSource {

    private static final String SCHOOLS_DATABASE_NAME = BuildConfig.APPLICATION_ID + ".schools.database";

    protected final SchoolsDatabase schoolsDatabase;

    protected final GlobalPreferences globalPreferences;

    protected final SchoolDao schoolDao;

    public DataSourceImpl(Context applicationContext, GlobalPreferences globalPreferences) {
        this.globalPreferences = globalPreferences;
        schoolsDatabase = Room.databaseBuilder(applicationContext, SchoolsDatabase.class, SCHOOLS_DATABASE_NAME).build();
        schoolDao = schoolsDatabase.getSchoolDao();
    }

    public void closeConnections() {
        schoolsDatabase.close();
    }

    // TODO: loadSchools(byType:)
    @Override
    public Single<List<School>> loadSchools() {
        return Single.fromCallable(schoolDao::getAll)
                .map(roomSchools -> new ArrayList<>(roomSchools));
    }

    // TODO: rewriteAllSchools(byType:schools:)
    @Override
    public Completable rewriteAllSchools(List<School> schools) {
        return Observable.fromIterable(schools)
                .map(RoomSchool::new)
                .toList()
                .flatMapCompletable(roomSchools -> Completable.fromAction(() -> {
                    schoolDao.deleteAll();
                    schoolDao.insert(roomSchools);
                }));
    }

    @Override
    public String getSchoolsFileName() {
        switch (globalPreferences.getAppRegion()) {
            case FCM:
                return BuildConfig.SCHOOLS_FCM_FILE_NAME;
            case RMI:
                return BuildConfig.SCHOOLS_RMI_FILE_NAME;
        }
        throw new IllegalStateException();
    }
}
