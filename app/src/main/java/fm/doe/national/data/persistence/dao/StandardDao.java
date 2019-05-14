package fm.doe.national.data.persistence.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomStandard;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomStandard;

@Dao
public interface StandardDao {

    @Insert
    long insert(RoomStandard category);

    @Update
    void update(RoomStandard category);

    @Delete
    void delete(RoomStandard category);

    @Nullable
    @Query("SELECT * FROM RoomStandard WHERE uid = :id LIMIT 1")
    RoomStandard getById(long id);

    @Query("SELECT * FROM RoomStandard WHERE category_id = :categoryId")
    List<RoomStandard> getAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM RoomStandard WHERE category_id = :categoryId")
    void deleteAllForCategoryWithId(long categoryId);

    @Query("DELETE FROM RoomStandard WHERE uid = :id")
    void deleteById(long id);

    @Nullable
    @Transaction
    @Query("SELECT * FROM RoomStandard WHERE uid = :id LIMIT 1")
    RelativeRoomStandard getFilledById(long id);

    @Transaction
    @Query("SELECT * FROM RoomStandard WHERE category_id = :categoryId")
    List<RelativeRoomStandard> getAllFilledForCategoryWithId(long categoryId);
}
