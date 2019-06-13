package fm.doe.national.wash_core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.wash_core.data.persistence.entity.RoomWashSurvey;
import fm.doe.national.wash_core.data.persistence.entity.relative.RelativeRoomSurvey;

@Dao
public interface SurveyDao {

    @Insert
    long insert(RoomWashSurvey survey);

    @Update
    void update(RoomWashSurvey survey);

    @Delete
    void delete(RoomWashSurvey survey);

    @Query("SELECT * FROM RoomWashSurvey WHERE region = :region")
    List<RoomWashSurvey> getAll(AppRegion region);

    @Nullable
    @Query("SELECT * FROM RoomWashSurvey WHERE uid = :id LIMIT 1")
    RoomWashSurvey getById(long id);

    @Query("DELETE FROM RoomWashSurvey")
    void deleteAll();

    @Query("DELETE FROM RoomWashSurvey WHERE region = :appRegion")
    void deleteAllForAppRegion(AppRegion appRegion);

    @Query("DELETE FROM RoomWashSurvey WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomWashSurvey WHERE uid = :id LIMIT 1")
    RelativeRoomSurvey getFilledById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomWashSurvey WHERE region = :region LIMIT 1")
    RelativeRoomSurvey getFirstFilled(AppRegion region);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomWashSurvey WHERE region = :region")
    List<RelativeRoomSurvey> getAllFilled(AppRegion region);

}
