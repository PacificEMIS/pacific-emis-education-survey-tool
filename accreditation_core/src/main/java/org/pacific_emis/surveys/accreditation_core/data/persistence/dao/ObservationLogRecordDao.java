package org.pacific_emis.surveys.accreditation_core.data.persistence.dao;


import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomObservationLogRecord;

@Dao
public interface ObservationLogRecordDao {

    @Insert
    long insert(RoomObservationLogRecord record);

    @Update
    void update(RoomObservationLogRecord record);

    @Delete
    void delete(RoomObservationLogRecord record);

    @Nullable
    @Query("SELECT * FROM RoomObservationLogRecord WHERE uid = :id LIMIT 1")
    RoomObservationLogRecord getById(long id);

    @Query("SELECT * FROM RoomObservationLogRecord WHERE category_id = :categoryId")
    List<RoomObservationLogRecord> getAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM RoomObservationLogRecord WHERE category_id = :categoryId")
    void deleteAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM RoomObservationLogRecord WHERE uid = :id")
    void deleteById(long id);

}
