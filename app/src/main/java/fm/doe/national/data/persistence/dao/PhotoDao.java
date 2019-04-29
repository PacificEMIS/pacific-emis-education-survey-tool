package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.PersistencePhotoEntity;

@Dao
public interface PhotoDao {
    @Insert
    void insert(PersistencePhotoEntity photoEntity);

    @Update
    void update(PersistencePhotoEntity photoEntity);

    @Delete
    void delete(PersistencePhotoEntity photoEntity);

    @Nullable
    @Query("SELECT * FROM PersistencePhotoEntity WHERE uid = :id LIMIT 1")
    PersistencePhotoEntity getById(long id);

    @Query("SELECT * FROM PersistencePhotoEntity WHERE subcriteria_id = :subCriteriaId")
    List<PersistencePhotoEntity> getAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM PersistencePhotoEntity WHERE subcriteria_id = :subCriteriaId")
    void deleteAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM PersistenceSubCriteria WHERE uid = :id")
    void deleteById(long id);
}
