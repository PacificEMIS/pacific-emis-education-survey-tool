package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomSchool;

@Dao
public interface SchoolDao {

    @Insert
    void insert(RoomSchool school);

    @Update
    void update(RoomSchool school);

    @Delete
    void delete(RoomSchool school);

    @Query("SELECT * FROM RoomSchool")
    List<RoomSchool> getAll();

    @Nullable
    @Query("SELECT * FROM RoomSchool WHERE uid = :id LIMIT 1")
    RoomSchool getById(long id);

    @Query("DELETE FROM RoomSchool")
    void deleteAll();
}
