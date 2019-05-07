package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceSubCriteria;

@Dao
public interface SubCriteriaDao {
    @Insert
    long insert(PersistenceSubCriteria subcriteria);

    @Update
    void update(PersistenceSubCriteria subcriteria);

    @Delete
    void delete(PersistenceSubCriteria subcriteria);

    @Nullable
    @Query("SELECT * FROM PersistenceSubCriteria WHERE uid = :id LIMIT 1")
    PersistenceSubCriteria getById(long id);

    @Query("SELECT * FROM PersistenceSubCriteria WHERE criteria_id = :criteriaId")
    List<PersistenceSubCriteria> getAllForCriteriaWithId(long criteriaId);

    @Query("DELETE FROM PersistenceSubCriteria WHERE criteria_id = :criteriaId")
    void deleteAllForCriteriaWithId(long criteriaId);

    @Query("DELETE FROM PersistenceSubCriteria WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM PersistenceSubCriteria WHERE uid = :id LIMIT 1")
    RelativePersistenceSubCriteria getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM PersistenceSubCriteria WHERE criteria_id = :criteriaId")
    List<RelativePersistenceSubCriteria> getAllFilledForCriteriaWithId(long criteriaId);
}
