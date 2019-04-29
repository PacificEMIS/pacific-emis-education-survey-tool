package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import fm.doe.national.data.persistence.entity.PersistencePhoto;

@Dao
public interface PhotoDao {
    @Insert
    void insert(PersistencePhoto photoEntity);

    @Update
    void update(PersistencePhoto photoEntity);

    @Delete
    void delete(PersistencePhoto photoEntity);

    @Nullable
    @Query("SELECT * FROM PersistencePhoto WHERE uid = :id LIMIT 1")
    PersistencePhoto getById(long id);

    @Query("SELECT * FROM PersistencePhoto WHERE answer_id = :answerId")
    List<PersistencePhoto> getAllForAnswerWithId(long answerId);

    @Query("DELETE FROM PersistencePhoto WHERE answer_id = :answerId")
    void deleteAllForAnswerWithId(long answerId);

    @Query("DELETE FROM PersistenceSubCriteria WHERE uid = :id")
    void deleteById(long id);
}
