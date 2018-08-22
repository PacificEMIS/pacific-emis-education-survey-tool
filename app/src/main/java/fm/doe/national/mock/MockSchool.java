package fm.doe.national.mock;

import android.support.annotation.NonNull;

import java.util.List;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
/**
 * Created by Alexander Chibirev on 8/17/2018.
 */

public class MockSchool {
    private String name;
    private int year;
    @NonNull
    private List<SubCriteriaViewData> subCriterias;

    public MockSchool(String name, int year, @NonNull List<SubCriteriaViewData> subCriterias) {
        this.name = name;
        this.year = year;
        this.subCriterias = subCriterias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @NonNull
    public List<SubCriteriaViewData> getSubCriterias() {
        return subCriterias;
    }

    public void setSubCriterias(@NonNull List<SubCriteriaViewData> subCriterias) {
        this.subCriterias = subCriterias;
    }
}
