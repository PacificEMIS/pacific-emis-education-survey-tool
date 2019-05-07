package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceCriteria;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceCriteria;

@Dao
public interface CriteriaDao {
    @Insert
    long insert(PersistenceCriteria criteria);

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

    @Nullable
    @Transaction
    @Query("SELECT * FROM PersistenceCriteria WHERE uid = :id LIMIT 1")
    RelativePersistenceCriteria getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM PersistenceCriteria WHERE standard_id = :standardId")
    List<RelativePersistenceCriteria> getAllFilledForStandardWithId(long standardId);
}
