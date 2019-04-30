package fm.doe.national.data.persistence.dao;


import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceCategory;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceCategory;

@Dao
public interface CategoryDao {

    @Insert
    void insert(PersistenceCategory category);

    @Update
    void update(PersistenceCategory category);

    @Delete
    void delete(PersistenceCategory category);

    @Nullable
    @Query("SELECT * FROM PersistenceCategory WHERE uid = :id LIMIT 1")
    PersistenceCategory getById(long id);

    @Query("SELECT * FROM PersistenceCategory WHERE survey_id = :surveyId")
    List<PersistenceCategory> getAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM PersistenceCategory WHERE survey_id = :surveyId")
    void deleteAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM PersistenceCategory WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM PersistenceCategory WHERE uid = :id LIMIT 1")
    RelativePersistenceCategory getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM PersistenceCategory WHERE survey_id = :surveyId")
    List<RelativePersistenceCategory> getAllFilledForSurveyWithId(long surveyId);
}
