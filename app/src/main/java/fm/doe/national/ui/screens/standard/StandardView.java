package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.view_data.CriteriaViewData;

public interface StandardView extends BaseView {
    void bindGlobalInfo(String title, int index);
    void bindCriterias(@NonNull List<CriteriaViewData> criterias);
    void bindProgress(int answered, int total);
    void bindPrevStandard(String title, int index);
    void bindNextStandard(String title, int index);
}
