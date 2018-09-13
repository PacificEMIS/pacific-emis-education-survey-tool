package fm.doe.national.ui.screens.criterias;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CriteriasView extends BaseView {
    void setGlobalInfo(String title, int resourceIndex);
    void setCriterias(@NonNull List<Criteria> criterias);
    void setProgress(int answered, int total);
    void setPrevStandard(String title, int resourceIndex);
    void setNextStandard(String title, int resourceIndex);
    void setSurveyYear(int year);
    void setSchoolName(String schoolName);
    void setCategoryName(String categoryName);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void takePictureTo(@NonNull File toFile);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void notifySubCriteriaChanged(SubCriteria subCriteria);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCommentEditor(SubCriteria subCriteria);
}
