package fm.doe.national.data.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceCategory.class, parentColumns = "uid", childColumns = "category_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("category_id")})
public class PersistenceStandard {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "category_id")
    public long categoryId;

    public PersistenceStandard(String title, long categoryId, String suffix) {
        this.title = title;
        this.categoryId = categoryId;
        this.suffix = suffix;
    }
}
