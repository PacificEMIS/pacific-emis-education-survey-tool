package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceAnswer;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceAnswer;

@Dao
public interface AnswerDao {
    @Insert
    PersistenceAnswer insert(PersistenceAnswer answer);

    @Update
    PersistenceAnswer update(PersistenceAnswer answer);

    @Delete
    void delete(PersistenceAnswer answer);

    @Nullable
    @Query("SELECT * FROM PersistenceAnswer WHERE uid = :id LIMIT 1")
    PersistenceAnswer getById(long id);

    @Query("SELECT * FROM PersistenceAnswer WHERE sub_criteria_id = :subCriteriaId")
    List<PersistenceAnswer> getAllForSubCriteriaWithId(long subCriteriaId);

    @Nullable
    @Transaction
    @Query("SELECT * FROM PersistenceAnswer WHERE uid = :id LIMIT 1")
    RelativePersistenceAnswer getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM PersistenceAnswer WHERE sub_criteria_id = :subCriteriaId")
    List<RelativePersistenceAnswer> getAllFilledForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM PersistenceAnswer WHERE sub_criteria_id = :subCriteriaId")
    void deleteAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM PersistenceAnswer WHERE uid = :id")
    void deleteById(long id);
}