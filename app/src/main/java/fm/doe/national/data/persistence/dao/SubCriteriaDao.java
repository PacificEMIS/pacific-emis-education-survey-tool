package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.SubCriteria;

@Dao
public interface SubCriteriaDao {
    @Insert
    void insert(SubCriteria subcriteria);

    @Update
    void update(SubCriteria subcriteria);

    @Delete
    void delete(SubCriteria subcriteria);

    @Nullable
    @Query("SELECT * FROM subcriteria WHERE uid = :id LIMIT 1")
    SubCriteria getById(long id);

    @Query("SELECT * FROM subcriteria WHERE criteria_id = :criteriaId")
    List<SubCriteria> getAllForCriteriaWithId(long criteriaId);

    @Query("DELETE FROM subcriteria WHERE criteria_id = :criteriaId")
    void deleteAllForCriteriaWithId(long criteriaId);

    @Query("DELETE FROM subcriteria WHERE uid = :id")
    void deleteById(long id);
}
