package fm.doe.national.data.persistence.dao;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.Survey;

@Dao
public interface SurveyDao {

    @Insert
    void insert(Survey survey);

    @Update
    void update(Survey survey);

    @Delete
    void delete(Survey survey);

    @Query("SELECT * FROM survey")
    List<Survey> getAll();

    @Nullable
    @Query("SELECT * FROM survey WHERE uid = :id LIMIT 1")
    Survey getById(long id);

    @Query("DELETE FROM survey")
    void deleteAll();

    @Query("DELETE FROM survey WHERE uid = :id")
    void deleteById(long id);
}
