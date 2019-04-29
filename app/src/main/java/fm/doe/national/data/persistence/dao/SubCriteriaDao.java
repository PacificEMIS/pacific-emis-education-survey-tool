package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.PersistenceSubCriteria;

@Dao
public interface SubCriteriaDao {
    @Insert
    void insert(PersistenceSubCriteria subcriteria);

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
}
