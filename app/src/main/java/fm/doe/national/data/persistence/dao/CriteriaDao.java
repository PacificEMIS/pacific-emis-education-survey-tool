package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.PersistenceCriteria;

@Dao
public interface CriteriaDao {
    @Insert
    void insert(PersistenceCriteria criteria);

    @Update
    void update(PersistenceCriteria criteria);

    @Delete
    void delete(PersistenceCriteria criteria);

    @Nullable
    @Query("SELECT * FROM PersistenceCriteria WHERE uid = :id LIMIT 1")
    PersistenceCriteria getById(long id);

    @Query("SELECT * FROM PersistenceCriteria WHERE standard_id = :standardId")
    List<PersistenceCriteria> getAllForStandardWithId(long standardId);

    @Query("DELETE FROM PersistenceCriteria WHERE standard_id = :standardId")
    void deleteAllForStandardWithId(long standardId);

    @Query("DELETE FROM PersistenceCriteria WHERE uid = :id")
    void deleteById(long id);
}
