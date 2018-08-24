package fm.doe.national.ui.listeners;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.view_data.SubCriteriaViewData;

public interface SubcriteriaStateChangeListener {
    void onSubCriteriaStateChanged(@NonNull SubCriteriaViewData viewData,
                                   @NonNull SubCriteria subCriteria,
                                   @Nullable Answer answer, Answer.State newState);
}
