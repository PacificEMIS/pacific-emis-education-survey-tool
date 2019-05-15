package fm.doe.national.ui.screens.criterias;

import android.view.View;

import androidx.annotation.NonNull;

import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.SubCriteria;

public interface SubcriteriaCallback {
    void onSubCriteriaStateChanged(@NonNull SubCriteria subCriteria, AnswerState newState);
    void onEditCommentClicked(SubCriteria subCriteria);
    void onAddCommentClicked(SubCriteria subCriteria);
    void onRemoveCommentClicked(SubCriteria subCriteria);
    void onAddPhotoClicked(SubCriteria subCriteria);
    void onRemovePhotoClicked(SubCriteria subCriteria, Photo photo);
    void onPhotoClicked(View anchor, Photo photo);
    void onMorePhotosClick(SubCriteria subCriteria);
}
