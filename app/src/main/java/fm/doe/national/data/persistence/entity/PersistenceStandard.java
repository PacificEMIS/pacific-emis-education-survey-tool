package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.Standard;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceCategory.class, parentColumns = "uid", childColumns = "category_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("category_id")})
public class PersistenceStandard implements Standard {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @ColumnInfo(name = "category_id")
    public long categoryId;

    @Ignore
    private List<Criteria> criterias;

    public PersistenceStandard(String title, long categoryId, String suffix) {
        this.title = title;
        this.categoryId = categoryId;
        this.suffix = suffix;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @NonNull
    @Override
    public List<Criteria> getCriterias() {
        return criterias;
    }

    @Override
    public long getId() {
        return uid;
    }

}
