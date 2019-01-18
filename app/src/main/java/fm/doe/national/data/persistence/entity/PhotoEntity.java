package fm.doe.national.data.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = SubCriteria.class, parentColumns = "uid", childColumns = "subcriteria_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("subcriteria_id")})
public class PhotoEntity {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String url;

    @ColumnInfo(name = "subcriteria_id")
    public long subCriteriaId;

    public PhotoEntity(String url, long subCriteriaId) {
        this.url = url;
        this.subCriteriaId = subCriteriaId;
    }
}
