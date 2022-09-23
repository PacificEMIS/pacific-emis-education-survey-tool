package org.pacific_emis.surveys.core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.pacific_emis.surveys.core.data.persistence.model.RoomSurveyLog;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.List;

@Dao
public interface SurveyLogsDao {

    @Insert
    void insert(RoomSurveyLog deletedSurvey);

    @Update
    void update(RoomSurveyLog subject);

    @Delete
    void delete(RoomSurveyLog subject);

    @Query("SELECT * FROM RoomSurveyLog WHERE appRegion = :appRegion")
    List<RoomSurveyLog> getAll(AppRegion appRegion);

    @Nullable
    @Query("SELECT * FROM RoomSurveyLog WHERE id = :id LIMIT 1")
    RoomSurveyLog getById(String id);

    @Query("DELETE FROM RoomSurveyLog")
    void deleteAll();

    @Query("DELETE FROM RoomSurveyLog WHERE appRegion = :appRegion")
    void deleteAllForAppRegion(AppRegion appRegion);

}
