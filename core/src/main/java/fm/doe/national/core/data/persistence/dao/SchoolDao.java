package fm.doe.national.core.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.core.data.persistence.entity.RoomSchool;

@Dao
public interface SchoolDao {

    @Insert
    void insert(RoomSchool school);

    @Insert
    void insert(List<RoomSchool> schools);

    @Update
    void update(RoomSchool school);

    @Delete
    void delete(RoomSchool school);

    @Query("SELECT * FROM RoomSchool")
    List<RoomSchool> getAll();

    @Nullable
    @Query("SELECT * FROM RoomSchool WHERE id = :id LIMIT 1")
    RoomSchool getById(String id);

    @Query("DELETE FROM RoomSchool")
    void deleteAll();
}
