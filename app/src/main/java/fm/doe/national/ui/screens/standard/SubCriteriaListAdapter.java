package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
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

    class SubCriteriaViewHolder extends ViewHolder implements SwitchableButton.StateChangedListener, View.OnLongClickListener {

        private final static String TAG_DIALOG = "TAG_DIALOG";

        @BindView(R.id.textview_alphabetical_numbering)
        TextView numberingTextView;

        @BindView(R.id.textview_question)
        TextView questionTextView;

        @BindView(R.id.switch_answer)
        SwitchableButton switchableButton;

        @BindView(R.id.textview_interview_questions)
        TextView interviewQuestionsTextView;

        @BindView(R.id.imageview_comment_button)
        View commentButtonView;

        @BindView(R.id.imageview_photo_button)
        View photoButtonView;

        @BindView(R.id.layout_comment)
        View commentView;

        @BindView(R.id.imageview_delete_button)
        View commentDeleteButton;

        @BindView(R.id.imageview_edit_button)
        View commentEditButton;

        @BindView(R.id.textview_comment)
        TextView commentTextView;

        private View popupView;
        private TextView hintView;

        SubCriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sub_criteria);
            switchableButton.setListener(this);
            popupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_hint, null);
            popupView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            hintView = popupView.findViewById(R.id.textview_hint);
        }

        @Override
        public void onBind(SubCriteria item) {
            questionTextView.setText(TextUtil.fixLineSeparators(item.getName()));
            numberingTextView.setText(getResources().getString(
                    R.string.criteria_char_icon_pattern,
                    TextUtil.convertIntToCharsIcons(getAdapterPosition())));

            SubCriteriaQuestion question = item.getSubCriteriaQuestion();
            String interviewQuestions = question.getInterviewQuestion();
            if (!TextUtils.isEmpty(interviewQuestions)) {
                interviewQuestionsTextView.setVisibility(View.VISIBLE);
                interviewQuestionsTextView.setText(interviewQuestions);
            } else {
                interviewQuestionsTextView.setVisibility(View.GONE);
            }

            switchableButton.setStateNotNotifying(convertToUiState(item.getAnswer().getState()));

            questionTextView.setOnLongClickListener(this);
            commentButtonView.setOnClickListener(this);
            photoButtonView.setOnClickListener(this);
            commentDeleteButton.setOnClickListener(this);
            commentEditButton.setOnClickListener(this);

            updateCommentVisibility(item.getAnswer().getComment());
        }

        @Override
        public void onStateChanged(SwitchableButton view, SwitchableButton.State state) {
            SubCriteria item = getItem();

            Answer.State previousState = item.getAnswer().getState();
            item.getAnswer().setState(convertFromUiState(state));

            notifyStateChanged(previousState);
        }

        @Override
        public boolean onLongClick(View v) {
            showHint();
            return true;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageview_comment_button:
                    showCommentDialog();
                    break;
                case R.id.imageview_photo_button:
                    // nothing for now
                    break;
                case R.id.imageview_delete_button:
                    updateCommentVisibility("");
                    notifyCommentChanged("");
                    break;
                case R.id.imageview_edit_button:
                    showCommentDialog();
                    break;
                default:
                    super.onClick(v);
            }
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
            if (answerStateChangedListener != null) answerStateChangedListener.onSubCriteriaAnswerChanged(
                    subCriteria, previousState);
        }

        private void notifyCommentChanged(String comment) {
            if (callback != null) callback.onSubCriteriaCommentChanged(getItem(), comment);
        }

        private void showHint() {
            String hint = getItem().getSubCriteriaQuestion().getHint();
            if (hint == null) hint = "";
            hintView.setText(TextUtil.fixLineSeparators(hint));
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    itemView.getMeasuredWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);

            popupView.measure(View.MeasureSpec.makeMeasureSpec(itemView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            popupWindow.showAsDropDown(itemView,
                    0,
                    -itemView.getMeasuredHeight() - popupView.getMeasuredHeight(),
                    Gravity.TOP);
        }

        private void showCommentDialog() {
            try {
                CommentDialog dialog = CommentDialog.create(getItem());
                dialog.setListener(comment -> {
                    updateCommentVisibility(comment);
                    notifyCommentChanged(comment);
                });
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), TAG_DIALOG);
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
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
    }

    public interface OnAnswerStateChangedListener {
        void onSubCriteriaAnswerChanged(@NonNull SubCriteria subCriteria, Answer.State previousState);
    }
}
