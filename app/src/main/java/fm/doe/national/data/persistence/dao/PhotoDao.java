package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.PhotoEntity;

@Dao
public interface PhotoDao {
    @Insert
    void insert(PhotoEntity photoEntity);

    @Update
    void update(PhotoEntity photoEntity);

    @Delete
    void delete(PhotoEntity photoEntity);

    @Nullable
    @Query("SELECT * FROM photoentity WHERE uid = :id LIMIT 1")
    PhotoEntity getById(long id);

    @Query("SELECT * FROM photoentity WHERE subcriteria_id = :subCriteriaId")
    List<PhotoEntity> getAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM photoentity WHERE subcriteria_id = :subCriteriaId")
    void deleteAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM subcriteria WHERE uid = :id")
    void deleteById(long id);
}
