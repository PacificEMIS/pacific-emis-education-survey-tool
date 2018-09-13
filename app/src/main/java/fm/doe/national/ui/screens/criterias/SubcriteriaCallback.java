package fm.doe.national.ui.screens.criterias;

import android.support.annotation.NonNull;
import android.view.View;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;

public interface SubcriteriaCallback {
    void onSubCriteriaStateChanged(@NonNull SubCriteria subCriteria, Answer.State previousState);
    void onEditCommentClicked(SubCriteria subCriteria);
    void onAddCommentClicked(SubCriteria subCriteria);
    void onRemoveCommentClicked(SubCriteria subCriteria);
    void onAddPhotoClicked(SubCriteria subCriteria);
    void onRemovePhotoClicked(SubCriteria subCriteria, String photoPath);
    void onPhotoClicked(View anchor, String photoPath);
}
