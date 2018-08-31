package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;

public interface SubcriteriaStateChangeListener {
    void onSubCriteriaStateChanged(@NonNull SubCriteria subCriteria, Answer.State previousState);
}
