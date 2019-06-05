package fm.doe.national.wash_core.data.persistence.dao;


import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.wash_core.data.persistence.entity.RoomGroup;
import fm.doe.national.wash_core.data.persistence.entity.relative.RelativeRoomGroup;

@Dao
public interface GroupDao {

    @Insert
    long insert(RoomGroup group);

    @Update
    void update(RoomGroup group);

    @Delete
    void delete(RoomGroup group);

    @Nullable
    @Query("SELECT * FROM RoomGroup WHERE uid = :id LIMIT 1")
    RoomGroup getById(long id);

    @Query("SELECT * FROM RoomGroup WHERE survey_id = :surveyId")
    List<RoomGroup> getAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM RoomGroup WHERE survey_id = :surveyId")
    void deleteAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM RoomGroup WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomGroup WHERE uid = :id LIMIT 1")
    RelativeRoomGroup getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomGroup WHERE survey_id = :surveyId")
    List<RelativeRoomGroup> getAllFilledForSurveyWithId(long surveyId);
}
