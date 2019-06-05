package fm.doe.national.wash_core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.wash_core.data.persistence.entity.RoomQuestion;
import fm.doe.national.wash_core.data.persistence.entity.relative.RelativeRoomQuestion;

@Dao
public interface QuestionDao {
    @Insert
    long insert(RoomQuestion question);

    @Update
    void update(RoomQuestion question);

    @Delete
    void delete(RoomQuestion question);

    @Nullable
    @Query("SELECT * FROM RoomQuestion WHERE uid = :id LIMIT 1")
    RoomQuestion getById(long id);

    @Query("SELECT * FROM RoomQuestion WHERE sub_group_id = :subGroupId")
    List<RoomQuestion> getAllForSubGroupWithId(long subGroupId);

    @Query("DELETE FROM RoomQuestion WHERE sub_group_id = :subGroupId")
    void deleteAllForSubGroupWithId(long subGroupId);

    @Query("DELETE FROM RoomQuestion WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomQuestion WHERE uid = :id LIMIT 1")
    RelativeRoomQuestion getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomQuestion WHERE sub_group_id = :subGroupId")
    List<RelativeRoomQuestion> getAllFilledForSubGroupWithId(long subGroupId);
}
