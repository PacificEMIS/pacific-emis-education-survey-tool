package fm.doe.national.data.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Survey;

@Entity
public class PersistenceSurvey {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "school_id")
    public String schoolId;

    @ColumnInfo(name = "school_name")
    public String schoolName;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @ColumnInfo(name = "version")
    public int version;

    @ColumnInfo(name = "type")
    public String type;

    @Ignore
    private List<Criteria> criterias = new ArrayList<>();

    public PersistenceSurvey(String schoolId, String schoolName, Date startDate) {
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

    public int getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }
}
