package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.PersistenceSchool;

@Dao
public interface SchoolDao {

    @Insert
    void insert(PersistenceSchool school);

    @Update
    void update(PersistenceSchool school);

    @Delete
    void delete(PersistenceSchool school);

    @Query("SELECT * FROM PersistenceSchool")
    List<PersistenceSchool> getAll();

    @Nullable
    @Query("SELECT * FROM PersistenceSchool WHERE uid = :id LIMIT 1")
    PersistenceSchool getById(long id);

    @Query("DELETE FROM PersistenceSchool")
    void deleteAll();
}
