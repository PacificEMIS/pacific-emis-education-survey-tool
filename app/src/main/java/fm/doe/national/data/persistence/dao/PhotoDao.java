package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomPhoto;

@Dao
public interface PhotoDao {
    @Insert
    long insert(RoomPhoto photoEntity);

    @Update
    void update(RoomPhoto photoEntity);

    @Delete
    void delete(RoomPhoto photoEntity);

    @Nullable
    @Query("SELECT * FROM RoomPhoto WHERE uid = :id LIMIT 1")
    RoomPhoto getById(long id);

    @Query("SELECT * FROM RoomPhoto WHERE answer_id = :answerId")
    List<RoomPhoto> getAllForAnswerWithId(long answerId);

    @Query("DELETE FROM RoomPhoto WHERE answer_id = :answerId")
    void deleteAllForAnswerWithId(long answerId);

    @Query("DELETE FROM RoomPhoto WHERE uid = :id")
    void deleteById(long id);
}
