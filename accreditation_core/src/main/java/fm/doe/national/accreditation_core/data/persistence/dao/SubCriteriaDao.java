package fm.doe.national.accreditation_core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.accreditation_core.data.persistence.entity.RoomSubCriteria;
import fm.doe.national.accreditation_core.data.persistence.entity.relative.RelativeRoomSubCriteria;

@Dao
public interface SubCriteriaDao {
    @Insert
    long insert(RoomSubCriteria subcriteria);

    @Update
    void update(RoomSubCriteria subcriteria);

    @Delete
    void delete(RoomSubCriteria subcriteria);

    @Nullable
    @Query("SELECT * FROM RoomSubCriteria WHERE uid = :id LIMIT 1")
    RoomSubCriteria getById(long id);

    @Query("SELECT * FROM RoomSubCriteria WHERE criteria_id = :criteriaId")
    List<RoomSubCriteria> getAllForCriteriaWithId(long criteriaId);

    @Query("DELETE FROM RoomSubCriteria WHERE criteria_id = :criteriaId")
    void deleteAllForCriteriaWithId(long criteriaId);

    @Query("DELETE FROM RoomSubCriteria WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomSubCriteria WHERE uid = :id LIMIT 1")
    RelativeRoomSubCriteria getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomSubCriteria WHERE criteria_id = :criteriaId")
    List<RelativeRoomSubCriteria> getAllFilledForCriteriaWithId(long criteriaId);
}
