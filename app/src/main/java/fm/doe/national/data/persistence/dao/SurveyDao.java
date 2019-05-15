package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomSurvey;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomSurvey;

@Dao
public interface SurveyDao {

    @Insert
    long insert(RoomSurvey survey);

    @Update
    void update(RoomSurvey survey);

    @Delete
    void delete(RoomSurvey survey);

    @Query("SELECT * FROM RoomSurvey")
    List<RoomSurvey> getAll();

    @Nullable
    @Query("SELECT * FROM RoomSurvey WHERE uid = :id LIMIT 1")
    RoomSurvey getById(long id);

    @Query("DELETE FROM RoomSurvey")
    void deleteAll();

    @Query("DELETE FROM RoomSurvey WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomSurvey WHERE uid = :id LIMIT 1")
    RelativeRoomSurvey getFilledById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomSurvey LIMIT 1")
    RelativeRoomSurvey getFirstFilled();

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomSurvey")
    List<RelativeRoomSurvey> getAllFilled();

}
