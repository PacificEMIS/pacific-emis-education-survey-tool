package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.Criteria;

@Dao
public interface CriteriaDao {
    @Insert
    void insert(Criteria criteria);

    @Update
    void update(Criteria criteria);

    @Delete
    void delete(Criteria criteria);

    @Nullable
    @Query("SELECT * FROM criteria WHERE uid = :id LIMIT 1")
    Criteria getById(long id);

    @Query("SELECT * FROM criteria WHERE standard_id = :standardId")
    List<Criteria> getAllForStandardWithId(long standardId);

    @Query("DELETE FROM criteria WHERE standard_id = :standardId")
    void deleteAllForStandardWithId(long standardId);

    @Query("DELETE FROM criteria WHERE uid = :id")
    void deleteById(long id);
}
