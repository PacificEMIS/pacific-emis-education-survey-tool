package fm.doe.national.data.persistence.dao;


import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fm.doe.national.data.persistence.entity.Category;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Nullable
    @Query("SELECT * FROM category WHERE uid = :id LIMIT 1")
    Category getById(long id);

    @Query("SELECT * FROM category WHERE survey_id = :surveyId")
    List<Category> getAllForSurveyWithId(long surveyId);

    @Query("DELETE FROM category WHERE survey_id = :surveyId")
    void deleteAllForSurveyWithId(long surveyId);
}
