package org.pacific_emis.surveys.wash_core.data.persistence.dao;


import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomSubGroup;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.relative.RelativeRoomSubGroup;

@Dao
public interface SubGroupDao {

    @Insert
    long insert(RoomSubGroup subGroup);

    @Update
    void update(RoomSubGroup subGroup);

    @Delete
    void delete(RoomSubGroup subGroup);

    @Nullable
    @Query("SELECT * FROM RoomSubGroup WHERE uid = :id LIMIT 1")
    RoomSubGroup getById(long id);

    @Query("SELECT * FROM RoomSubGroup WHERE group_id = :groupId")
    List<RoomSubGroup> getAllForGroupWithId(long groupId);

    @Query("DELETE FROM RoomSubGroup WHERE group_id = :groupId")
    void deleteAllForGroupWithId(long groupId);

    @Query("DELETE FROM RoomSubGroup WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomSubGroup WHERE uid = :id LIMIT 1")
    RelativeRoomSubGroup getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomSubGroup WHERE group_id = :groupId")
    List<RelativeRoomSubGroup> getAllFilledForGroupWithId(long groupId);
}
