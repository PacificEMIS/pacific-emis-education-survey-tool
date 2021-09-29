package org.pacific_emis.surveys.wash_core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomWashSurvey;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.relative.RelativeRoomSurvey;

import io.reactivex.Completable;

@Dao
public interface SurveyDao {

    @Insert
    long insert(RoomWashSurvey survey);

    @Update
    Completable update(RoomWashSurvey survey);

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

    @Query("SELECT * FROM RoomWashSurvey WHERE school_id = :schoolId AND region = :region AND survey_tag = :tag")
    List<RoomWashSurvey> getSurveys(String schoolId, AppRegion region, String tag);

}
