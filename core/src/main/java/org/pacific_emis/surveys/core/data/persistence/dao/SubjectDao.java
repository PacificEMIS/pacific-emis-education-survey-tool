package org.pacific_emis.surveys.core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.pacific_emis.surveys.core.data.persistence.model.RoomSubject;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.List;

@Dao
public interface SubjectDao {

    @Insert
    void insert(RoomSubject subject);

    @Insert
    void insert(List<RoomSubject> subjects);

    @Update
    void update(RoomSubject subject);

    @Delete
    void delete(RoomSubject subject);

    @Query("SELECT * FROM RoomSubject WHERE appRegion = :appRegion")
    List<RoomSubject> getAll(AppRegion appRegion);

    @Nullable
    @Query("SELECT * FROM RoomSubject WHERE id = :id LIMIT 1")
    RoomSubject getById(String id);

    @Query("DELETE FROM RoomSubject")
    void deleteAll();

    @Query("DELETE FROM RoomSubject WHERE appRegion = :appRegion")
    void deleteAllForAppRegion(AppRegion appRegion);
}
