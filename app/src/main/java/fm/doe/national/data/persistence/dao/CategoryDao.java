package fm.doe.national.data.persistence.dao;


import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomCategory;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomCategory;

@Dao
public interface CategoryDao {

    @Insert
    long insert(RoomCategory category);

    @Update
    void update(RoomCategory category);

    @Delete
    void delete(RoomCategory category);

    @Nullable
    @Query("SELECT * FROM RoomCategory WHERE uid = :id LIMIT 1")
    RoomCategory getById(long id);

    @Query("SELECT * FROM RoomCategory WHERE survey_id = :surveyId")
    List<RoomCategory> getAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM RoomCategory WHERE survey_id = :surveyId")
    void deleteAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM RoomCategory WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomCategory WHERE uid = :id LIMIT 1")
    RelativeRoomCategory getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomCategory WHERE survey_id = :surveyId")
    List<RelativeRoomCategory> getAllFilledForSurveyWithId(long surveyId);
}
