package fm.doe.national.ui.screens.criterias;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;
import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableSubCriteria;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CriteriasView extends BaseView {

    void setGlobalInfo(String title, @Nullable String resourceIndex);

    void setCriterias(@NonNull List<MutableCriteria> criterias);

    void setProgress(int answered, int total);

    void setPrevStandard(String title, @Nullable String resourceIndex);

    void setNextStandard(String title, @Nullable String resourceIndex);

    void setSurveyDate(Date date);

    void setSchoolName(String schoolName);

    void setCategoryName(String categoryName);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void takePictureTo(@NonNull File toFile);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void notifySubCriteriaChanged(MutableSubCriteria subCriteria);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void notifyCriteriaChanged(MutableCriteria criteria);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCommentEditor(MutableSubCriteria subCriteria);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPhotos(MutableSubCriteria subCriteria);

}
