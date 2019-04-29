package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.PersistenceStandard;

@Dao
public interface StandardDao {

    @Insert
    void insert(PersistenceStandard category);

    @Update
    void update(PersistenceStandard category);

    @Delete
    void delete(PersistenceStandard category);

    @Nullable
    @Query("SELECT * FROM PersistenceStandard WHERE uid = :id LIMIT 1")
    PersistenceStandard getById(long id);

    @Query("SELECT * FROM PersistenceStandard WHERE category_id = :categoryId")
    List<PersistenceStandard> getAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM PersistenceStandard WHERE category_id = :categoryId")
    void deleteAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM PersistenceStandard WHERE uid = :id")
    void deleteById(long id);
}
