package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.PersistenceSurvey;

@Dao
public interface SurveyDao {

    @Insert
    void insert(PersistenceSurvey survey);

    @Update
    void update(PersistenceSurvey survey);

    @Delete
    void delete(PersistenceSurvey survey);

    @Query("SELECT * FROM PersistenceSurvey")
    List<PersistenceSurvey> getAll();

    @Nullable
    @Query("SELECT * FROM PersistenceSurvey WHERE uid = :id LIMIT 1")
    PersistenceSurvey getById(long id);

    @Query("DELETE FROM PersistenceSurvey")
    void deleteAll();

    @Query("DELETE FROM PersistenceSurvey WHERE uid = :id")
    void deleteById(long id);
}
