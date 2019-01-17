package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.Standard;

@Dao
public interface StandardDao {

    @Insert
    void insert(Standard category);

    @Update
    void update(Standard category);

    @Delete
    void delete(Standard category);

    @Nullable
    @Query("SELECT * FROM standard WHERE uid = :id LIMIT 1")
    Standard getById(long id);

    @Query("SELECT * FROM standard WHERE category_id = :categoryId")
    List<Standard> getAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM standard WHERE category_id = :categoryId")
    void deleteAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM standard WHERE uid = :id")
    void deleteById(long id);
}
