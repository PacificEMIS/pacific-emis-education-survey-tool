package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.ui.screens.base.BaseView;

public interface StandardView extends BaseView {
    void setGlobalInfo(String title, int resourceIndex);
    void setCriterias(@NonNull List<Criteria> criterias);
    void setProgress(int answered, int total);
    void setPrevStandard(String title, int resourceIndex);
    void setNextStandard(String title, int resourceIndex);
    void setSurveyYear(int year);
    void setSchoolName(String schoolName);
}
