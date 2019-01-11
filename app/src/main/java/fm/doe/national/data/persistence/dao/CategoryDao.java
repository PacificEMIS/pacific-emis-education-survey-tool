package fm.doe.national.data.persistence.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import fm.doe.national.data.persistence.entity.Category;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);
}
