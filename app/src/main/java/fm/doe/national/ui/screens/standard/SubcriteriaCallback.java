package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.view.View;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;

public interface SubcriteriaCallback {
    void onSubCriteriaCallForHint(View anchor, SubCriteria subCriteria);
    void onSubCriteriaStateChanged(@NonNull SubCriteria subCriteria, Answer.State previousState);
    void onSubCriteriaCallForCommentEdit(SubCriteria subCriteria);
}
