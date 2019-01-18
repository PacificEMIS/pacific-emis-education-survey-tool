package fm.doe.national.data.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Standard.class, parentColumns = "uid", childColumns = "standard_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("standard_id")})
public class Criteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "standard_id")
    public long standardId;

    public Criteria(String title, long standardId, String suffix) {
        this.title = title;
        this.standardId = standardId;
        this.suffix = suffix;
    }

}

