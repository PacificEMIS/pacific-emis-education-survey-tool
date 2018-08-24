package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.view_data.CriteriaViewData;

public interface StandardView extends BaseView {
    void setGlobalInfo(String title, int resourceIndex);
    void setCriterias(@NonNull List<CriteriaViewData> criterias);
    void setProgress(int answered, int total);
    void setPrevStandard(String title, int resourceIndex);
    void setNextStandard(String title, int resourceIndex);
}
