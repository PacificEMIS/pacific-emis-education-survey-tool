package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.view.View;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;

public interface SubcriteriaCallback {
    void onSubCriteriaStateChanged(@NonNull SubCriteria subCriteria, Answer.State previousState);
    void onSubCriteriaCommentChanged(SubCriteria subCriteria, String newComment);
    void onAddPhotoClicked(SubCriteria subCriteria);
    void onRemovePhotoClicked(SubCriteria subCriteria, String photoPath);
    void onPhotoClicked(View anchor, String photoPath);
}
