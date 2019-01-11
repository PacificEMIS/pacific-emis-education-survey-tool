package fm.doe.national.data.persistence.dao;

import java.util.List;

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

    @Query("SELECT * FROM survey")
    List<Survey> getAll();

    @Query("DELETE FROM survey")
    void deleteAll();

    @Update
    void update(Survey survey);

    @Delete
    void delete(Survey survey);
}
