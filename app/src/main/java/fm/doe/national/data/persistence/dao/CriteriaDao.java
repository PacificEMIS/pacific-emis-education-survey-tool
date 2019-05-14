package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomCriteria;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomCriteria;

@Dao
public interface CriteriaDao {
    @Insert
    long insert(RoomCriteria criteria);

    @Update
    void update(RoomCriteria criteria);

    @Delete
    void delete(RoomCriteria criteria);

    @Nullable
    @Query("SELECT * FROM RoomCriteria WHERE uid = :id LIMIT 1")
    RoomCriteria getById(long id);

    @Query("SELECT * FROM RoomCriteria WHERE standard_id = :standardId")
    List<RoomCriteria> getAllForStandardWithId(long standardId);

    @Query("DELETE FROM RoomCriteria WHERE standard_id = :standardId")
    void deleteAllForStandardWithId(long standardId);

    @Query("DELETE FROM RoomCriteria WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomCriteria WHERE uid = :id LIMIT 1")
    RelativeRoomCriteria getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomCriteria WHERE standard_id = :standardId")
    List<RelativeRoomCriteria> getAllFilledForStandardWithId(long standardId);
}
