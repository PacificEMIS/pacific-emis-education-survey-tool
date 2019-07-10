package fm.doe.national.survey_core.ui.survey;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.CallSuper;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.core.data.model.Answerable;
import fm.doe.national.core.data.model.BaseAnswer;
import fm.doe.national.survey_core.R;

public abstract class BaseQuestionsAdapter<T extends Answerable> extends BaseListAdapter<T> {

    private final Listener<T> listener;

    public BaseQuestionsAdapter(Listener<T> listener) {
        this.listener = listener;
    }

    public class BaseQuestionViewHolder extends ViewHolder {

        private final View commentView = findViewById(R.id.layout_comment);
        private final TextView commentTextView = findViewById(R.id.textview_comment);
        private final ImageButton deleteCommentImageButton = findViewById(R.id.imagebutton_delete_comment);
        private final ImageButton editCommentImageButton = findViewById(R.id.imagebutton_edit_comment);
        private final ImageButton newPhotoImageButton = findViewById(R.id.imagebutton_new_photo);
        private final ImageButton newCommentImageButton = findViewById(R.id.imagebutton_new_comment);

        public BaseQuestionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            deleteCommentImageButton.setOnClickListener(this);
            editCommentImageButton.setOnClickListener(this);
            newPhotoImageButton.setOnClickListener(this);
            newCommentImageButton.setOnClickListener(this);
            commentView.setOnClickListener(this);
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
        }

        @Override
        public void onClick(View v) {
            T item = getItem();
            int position = getAdapterPosition();

            if (v == deleteCommentImageButton) {
                listener.onDeleteCommentPressed(item, position);
            } else if (v == editCommentImageButton || v == newCommentImageButton || v == commentView) {
                listener.onCommentPressed(item, position);
            } else if (v == newPhotoImageButton) {
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
