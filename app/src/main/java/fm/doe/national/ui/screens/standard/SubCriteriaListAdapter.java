package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;
import fm.doe.national.ui.custom_views.SwitchableButton;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.TextUtil;

public class SubCriteriaListAdapter extends BaseAdapter<SubCriteria> {

    @Nullable
    private SubcriteriaCallback callback = null;

    public void setCallback(@Nullable SubcriteriaCallback callback) {
        this.callback = callback;
    }

    @Nullable
    private OnAnswerStateChangedListener answerStateChangedListener = null;

    public void setAnswerStateChangedListener(@Nullable OnAnswerStateChangedListener answerStateChangedListener) {
        this.answerStateChangedListener = answerStateChangedListener;
    }

    @Override
    protected SubCriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new SubCriteriaViewHolder(parent);
    }

    class SubCriteriaViewHolder extends ViewHolder implements SwitchableButton.StateChangedListener {

        private final PhotosAdapter photosAdapter = new PhotosAdapter();

        private final View popupView;
        private final TextView hintView;
        
        @BindView(R.id.textview_alphabetical_numbering)
        TextView numberingTextView;

        @BindView(R.id.textview_question)
        TextView questionTextView;

        @BindView(R.id.switchablebutton_answer)
        SwitchableButton switchableButton;

        @BindView(R.id.textview_interview_questions)
        TextView interviewQuestionsTextView;

        @BindView(R.id.imageview_comment_button)
        View commentButtonView;

        @BindView(R.id.imageview_photo_button)
        View photoButtonView;

        @BindView(R.id.layout_comment)
        View commentView;

        @BindView(R.id.imagebutton_delete)
        View commentDeleteButton;

        @BindView(R.id.imageview_edit_button)
        View commentEditButton;

        @BindView(R.id.textview_comment)
        TextView commentTextView;

        @BindView(R.id.recyclerview_photos)
        RecyclerView photosRecyclerView;

        SubCriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sub_criteria);
            switchableButton.setListener(this);
            popupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_hint, parent, false);
            hintView = popupView.findViewById(R.id.textview_hint);

            photosRecyclerView.setAdapter(photosAdapter);
            photosAdapter.setCallback(callback);
        }

        @Override
        public void onBind(SubCriteria item) {
            Answer answer = item.getAnswer();
            SubCriteriaQuestion question = item.getSubCriteriaQuestion();
            String interviewQuestions = question.getInterviewQuestion();

            questionTextView.setText(item.getName());
            numberingTextView.setText(getResources().getString(
                    R.string.criteria_char_icon_pattern,
                    TextUtil.convertIntToCharsIcons(getAdapterPosition())));

            if (!TextUtils.isEmpty(interviewQuestions)) {
                interviewQuestionsTextView.setVisibility(View.VISIBLE);
                interviewQuestionsTextView.setText(interviewQuestions);
            } else {
                interviewQuestionsTextView.setVisibility(View.GONE);
            }
            switchableButton.setStateNotNotifying(convertToUiState(answer.getState()));

            updateCommentVisibility(answer.getComment());
            updatePhotosVisibility(answer.getPhotos());
        }

        @OnLongClick(R.id.textview_question)
        public boolean onViewLongClick(View v) {
            showHint();
            return true;
        }

        @OnClick({
                R.id.imageview_comment_button,
                R.id.imageview_photo_button,
                R.id.imagebutton_delete,
                R.id.imageview_edit_button,
                R.id.imagebutton_add_photo
        })
        public void onViewClick(View v) {
            if (callback != null) {
                SubCriteria item = getItem();
                switch (v.getId()) {
                    case R.id.imageview_comment_button:
                        callback.onAddCommentClicked(item);
                        break;
                    case R.id.imageview_photo_button:
                        callback.onAddPhotoClicked(item);
                        break;
                    case R.id.imagebutton_delete:
                        callback.onRemoveCommentClicked(item);
                        break;
                    case R.id.imageview_edit_button:
                        callback.onEditCommentClicked(item);
                        break;
                    case R.id.imagebutton_add_photo:
                        callback.onAddPhotoClicked(item);
                        break;
                }
            }

        }

        @Override
        public void onStateChanged(SwitchableButton view, SwitchableButton.State state) {
            Answer answer = getItem().getAnswer();

            Answer.State previousState = answer.getState();
            answer.setState(convertFromUiState(state));

            notifyStateChanged(previousState);
        }

        private SwitchableButton.State convertToUiState(Answer.State state) {
            switch (state) {
                case NOT_ANSWERED:
                    return SwitchableButton.State.NEUTRAL;
                case NEGATIVE:
                    return SwitchableButton.State.NEGATIVE;
                case POSITIVE:
                    return SwitchableButton.State.POSITIVE;
            }
            return SwitchableButton.State.NEUTRAL; // unreachable code
        }

        private Answer.State convertFromUiState(SwitchableButton.State state) {
            switch (state) {
                case NEUTRAL:
                    return Answer.State.NOT_ANSWERED;
                case NEGATIVE:
                    return Answer.State.NEGATIVE;
                case POSITIVE:
                    return Answer.State.POSITIVE;
            }
            return Answer.State.NOT_ANSWERED; // unreachable code
        }

        private void notifyStateChanged(Answer.State previousState) {
            SubCriteria subCriteria = getItem();
            if (callback != null) callback.onSubCriteriaStateChanged(subCriteria, previousState);
            if (answerStateChangedListener != null)
                answerStateChangedListener.onSubCriteriaAnswerChanged(subCriteria, previousState);
        }

        private void showHint() {
            hintView.setText(getItem().getSubCriteriaQuestion().getHint());
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    itemView.getMeasuredWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);

            // need to measure view before it rendered
            // showAsDropDown cannot draw popup above anchor in ViewHolder
            // so just offset it manually
            popupView.measure(View.MeasureSpec.makeMeasureSpec(itemView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            popupWindow.showAsDropDown(itemView,
                    0,
                    -itemView.getMeasuredHeight() - popupView.getMeasuredHeight(),
                    Gravity.TOP);
        }

        private void updateCommentVisibility(@Nullable String currentComment) {
            if (TextUtils.isEmpty(currentComment)) {
                commentView.setVisibility(View.GONE);
                commentButtonView.setVisibility(View.VISIBLE);
            } else {
                commentView.setVisibility(View.VISIBLE);
                commentButtonView.setVisibility(View.GONE);
                commentTextView.setText(currentComment);
            }
        }

        private void updatePhotosVisibility(List<String> photos) {
            if (photos.isEmpty()) {
                photosRecyclerView.setVisibility(View.GONE);
                photoButtonView.setVisibility(View.VISIBLE);
            } else {
                photosRecyclerView.setVisibility(View.VISIBLE);
                photoButtonView.setVisibility(View.GONE);
                photosAdapter.setParentSubCriteria(getItem());
                photosAdapter.setItems(photos);
            }
        }
    }

    public interface OnAnswerStateChangedListener {
        void onSubCriteriaAnswerChanged(@NonNull SubCriteria subCriteria, Answer.State previousState);
    }
}
