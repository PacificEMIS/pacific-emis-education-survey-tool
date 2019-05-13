package fm.doe.national.ui.screens.criterias;

import android.view.View;

import androidx.annotation.NonNull;

import fm.doe.national.data.model.mutable.MutablePhoto;
import fm.doe.national.data.model.mutable.MutableSubCriteria;

public interface SubcriteriaCallback {
    void onSubCriteriaStateChanged(@NonNull MutableSubCriteria subCriteria);
    void onEditCommentClicked(MutableSubCriteria subCriteria);
    void onAddCommentClicked(MutableSubCriteria subCriteria);
    void onRemoveCommentClicked(MutableSubCriteria subCriteria);
    void onAddPhotoClicked(MutableSubCriteria subCriteria);
    void onRemovePhotoClicked(MutableSubCriteria subCriteria, MutablePhoto photo);
    void onPhotoClicked(View anchor, MutablePhoto photo);
    void onMorePhotosClick(MutableSubCriteria subCriteria);
}
