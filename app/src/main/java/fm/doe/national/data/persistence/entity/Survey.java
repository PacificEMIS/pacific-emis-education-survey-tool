package fm.doe.national.data.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import fm.doe.national.data.data_source.models.Criteria;

@Entity
public class Survey {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "school_id")
    public String schoolId;

    @ColumnInfo(name = "school_name")
    public String schoolName;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @Ignore
    private List<Criteria> criterias = new ArrayList<>();

    public Survey(String schoolId, String schoolName, Date startDate) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.startDate = startDate;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }
}
