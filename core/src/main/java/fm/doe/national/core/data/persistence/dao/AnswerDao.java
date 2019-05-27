package fm.doe.national.core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.core.data.persistence.entity.RoomAnswer;
import fm.doe.national.core.data.persistence.entity.relative.RelativeRoomAnswer;

@Dao
public interface AnswerDao {
    @Insert
    long insert(RoomAnswer answer);

    @Update
    void update(RoomAnswer answer);

    @Delete
    void delete(RoomAnswer answer);

    @Nullable
    @Query("SELECT * FROM RoomAnswer WHERE uid = :id LIMIT 1")
    RoomAnswer getById(long id);

    @Query("SELECT * FROM RoomAnswer WHERE sub_criteria_id = :subCriteriaId")
    List<RoomAnswer> getAllForSubCriteriaWithId(long subCriteriaId);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomAnswer WHERE uid = :id LIMIT 1")
    RelativeRoomAnswer getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomAnswer WHERE sub_criteria_id = :subCriteriaId")
    List<RelativeRoomAnswer> getAllFilledForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM RoomAnswer WHERE sub_criteria_id = :subCriteriaId")
    void deleteAllForSubCriteriaWithId(long subCriteriaId);

    @Query("DELETE FROM RoomAnswer WHERE uid = :id")
    void deleteById(long id);
}