package org.pacific_emis.surveys.survey_core.ui.survey;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.data.model.Answerable;
import org.pacific_emis.surveys.core.data.model.BaseAnswer;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.survey_core.R;

public abstract class BaseQuestionsAdapter<T extends Answerable> extends BaseListAdapter<T> {

    private final Listener<T> listener;

    public BaseQuestionsAdapter(Listener<T> listener) {
        this.listener = listener;
    }

    public class BaseQuestionViewHolder extends ViewHolder {

        private final View commentView = findViewById(R.id.layout_comment);
        private final View photosView = findViewById(R.id.layout_photos);
        private final TextView commentTextView = findViewById(R.id.textview_comment);
        private final ImageButton deleteCommentImageButton = findViewById(R.id.imagebutton_delete_comment);
        private final ImageButton editCommentImageButton = findViewById(R.id.imagebutton_edit_comment);
        private final ImageButton newPhotoImageButton = findViewById(R.id.imagebutton_new_photo);
        private final ImageButton photosImageButton = findViewById(R.id.imagebutton_photos);
        private final ImageButton newCommentImageButton = findViewById(R.id.imagebutton_new_comment);
        private final RecyclerView photosRecyclerView = findViewById(R.id.recyclerview_photos);
        private final PhotosPreviewAdapter photosPreviewAdapter = new PhotosPreviewAdapter();

        public BaseQuestionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            deleteCommentImageButton.setOnClickListener(this);
            editCommentImageButton.setOnClickListener(this);
            newPhotoImageButton.setOnClickListener(this);
            photosImageButton.setOnClickListener(this);
            newCommentImageButton.setOnClickListener(this);
            commentView.setOnClickListener(this);
            photosRecyclerView.setAdapter(photosPreviewAdapter);
        }

        @CallSuper
        @Override
        protected void onBind(T item) {
            BaseAnswer answer = item.getAnswer();

            if (answer == null) {
                commentView.setVisibility(View.GONE);
                newPhotoImageButton.setVisibility(View.GONE);
                newCommentImageButton.setVisibility(View.GONE);
                return;
            }

            String comment = answer.getComment();
            if (TextUtils.isEmpty(comment)) {
                commentView.setVisibility(View.GONE);
                newCommentImageButton.setVisibility(View.VISIBLE);
            } else {
                commentView.setVisibility(View.VISIBLE);
                newCommentImageButton.setVisibility(View.GONE);
                commentTextView.setText(comment);
            }

            List<? extends Photo> photos = answer.getPhotos();
            if (CollectionUtils.isEmpty(photos)) {
                photosView.setVisibility(View.GONE);
                newPhotoImageButton.setVisibility(View.VISIBLE);
            } else {
                photosView.setVisibility(View.VISIBLE);
                newPhotoImageButton.setVisibility(View.GONE);
                photosPreviewAdapter.setItems(photos.stream().map(p -> (Photo) p).collect(Collectors.toList()));
            }
        }

        @Override
        public void onClick(View v) {
            T item = getItem();
            int position = getAdapterPosition();

            if (v == deleteCommentImageButton) {
                listener.onDeleteCommentPressed(item, position);
            } else if (v == editCommentImageButton || v == newCommentImageButton || v == commentView) {
                listener.onCommentPressed(item, position);
            } else if (v == newPhotoImageButton || v == photosImageButton) {
                listener.onPhotosPressed(item, position);
            } else {
                super.onClick(v);
            }
        }
    }

    public interface Listener<T extends Answerable> {
        void onCommentPressed(T item, int position);

        void onPhotosPressed(T item, int position);

        void onDeleteCommentPressed(T item, int position);
    }
}
