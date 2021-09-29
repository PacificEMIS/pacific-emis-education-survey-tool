package org.pacific_emis.surveys.core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.pacific_emis.surveys.core.data.persistence.model.RoomTeacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.List;

@Dao
public interface TeacherDao {

    @Insert
    void insert(RoomTeacher teacher);

    @Insert
    void insert(List<RoomTeacher> teachers);

    @Update
    void update(RoomTeacher teacher);

    @Delete
    void delete(RoomTeacher teacher);

    @Query("SELECT * FROM RoomTeacher WHERE appRegion = :appRegion")
    List<RoomTeacher> getAll(AppRegion appRegion);

    @Nullable
    @Query("SELECT * FROM RoomTeacher WHERE id = :id LIMIT 1")
    RoomTeacher getById(int id);

    @Query("DELETE FROM RoomTeacher")
    void deleteAll();

    @Query("DELETE FROM RoomTeacher WHERE appRegion = :appRegion")
    void deleteAllForAppRegion(AppRegion appRegion);
}
