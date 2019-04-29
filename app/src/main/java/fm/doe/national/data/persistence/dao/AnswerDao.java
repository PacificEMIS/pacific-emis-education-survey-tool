package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceAnswer;

@Dao
public interface AnswerDao {
    @Insert
    void insert(PersistenceAnswer answer);

    @Update
    void update(PersistenceAnswer answer);

    @Delete
    void delete(PersistenceAnswer answer);

    @Nullable
    @Query("SELECT * FROM PersistenceAnswer WHERE uid = :id LIMIT 1")
    PersistenceAnswer getById(long id);

    @Query("SELECT * FROM PersistenceAnswer WHERE sub_criteria_id = :subCriteriaId")
    List<PersistenceAnswer> getAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM PersistenceAnswer WHERE sub_criteria_id = :subCriteriaId")
    void deleteAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM PersistenceAnswer WHERE uid = :id")
    void deleteById(long id);
}