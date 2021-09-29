package org.pacific_emis.surveys.accreditation_core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.relative.RelativeRoomSurvey;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import io.reactivex.Completable;

@Dao
public interface SurveyDao {

    @Insert
    long insert(RoomAccreditationSurvey survey);

    @Update
    Completable update(RoomAccreditationSurvey survey);

    @Delete
    void delete(RoomAccreditationSurvey survey);

    @Query("SELECT * FROM RoomAccreditationSurvey WHERE region = :region")
    List<RoomAccreditationSurvey> getAll(AppRegion region);

    @Nullable
    @Query("SELECT * FROM RoomAccreditationSurvey WHERE uid = :id LIMIT 1")
    RoomAccreditationSurvey getById(long id);

    @Query("DELETE FROM RoomAccreditationSurvey")
    void deleteAll();

    @Query("DELETE FROM RoomAccreditationSurvey WHERE region = :appRegion")
    void deleteAllForAppRegion(AppRegion appRegion);

    @Query("DELETE FROM RoomAccreditationSurvey WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomAccreditationSurvey WHERE uid = :id LIMIT 1")
    RelativeRoomSurvey getFilledById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomAccreditationSurvey WHERE region = :appRegion LIMIT 1")
    RelativeRoomSurvey getFirstFilled(AppRegion appRegion);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomAccreditationSurvey WHERE region = :region")
    List<RelativeRoomSurvey> getAllFilled(AppRegion region);

    @Query("SELECT * FROM RoomAccreditationSurvey WHERE school_id = :schoolId AND region = :region AND survey_tag = :tag")
    List<RoomAccreditationSurvey> getSurveys(String schoolId, AppRegion region, String tag);

}
