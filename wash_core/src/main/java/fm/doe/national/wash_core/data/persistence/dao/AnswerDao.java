package fm.doe.national.wash_core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.wash_core.data.persistence.entity.RoomAnswer;
import fm.doe.national.wash_core.data.persistence.entity.relative.RelativeRoomAnswer;

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

    @Query("SELECT * FROM RoomAnswer WHERE question_id = :questionId")
    List<RoomAnswer> getAllForQuestionWithId(long questionId);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomAnswer WHERE uid = :id LIMIT 1")
    RelativeRoomAnswer getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomAnswer WHERE question_id = :questionId")
    List<RelativeRoomAnswer> getAllFilledForQuestionWithId(long questionId);

    @Query("DELETE FROM RoomAnswer WHERE question_id = :questionId")
    void deleteAllForQuestionWithId(long questionId);

    @Query("DELETE FROM RoomAnswer WHERE uid = :id")
    void deleteById(long id);
}