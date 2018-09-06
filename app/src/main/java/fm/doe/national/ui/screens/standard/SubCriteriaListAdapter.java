package fm.doe.national.ui.screens.standard;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;
import fm.doe.national.ui.custom_views.SwitchableButton;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.TextUtil;

public class SubCriteriaListAdapter extends BaseAdapter<SubCriteria> {

    private List<SubcriteriaCallback> subscribers = new ArrayList<>();

    public void clearSubscribers() {
        subscribers.clear();
    }

    public void unsubscribeOnChanges(SubcriteriaCallback listener) {
        subscribers.remove(listener);
    }

    public void subscribeOnChanges(SubcriteriaCallback listener) {
        subscribers.add(listener);
    }

    public void addSubscribers(List<SubcriteriaCallback> subscribers) {
        this.subscribers.addAll(subscribers);
    }

    @Override
    protected SubCriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new SubCriteriaViewHolder(parent);
    }

    class SubCriteriaViewHolder extends ViewHolder implements SwitchableButton.StateChangedListener, View.OnLongClickListener {

        @BindView(R.id.textview_alphabetical_numbering)
        TextView numberingTextView;

        @BindView(R.id.textview_question)
        TextView questionTextView;

        @BindView(R.id.switch_answer)
        SwitchableButton switchableButton;

        @BindView(R.id.textview_interview_questions)
        TextView interviewQuestionsTextView;

        @BindView(R.id.textview_comment_button)
        TextView commentButtonTextView;

        SubCriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sub_criteria);
            switchableButton.setListener(this);
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
            commentButtonTextView.setOnClickListener(this);
        }

        @Override
        public void onStateChanged(SwitchableButton view, SwitchableButton.State state) {
            SubCriteria item = getItem();

            Answer.State previousState = item.getAnswer().getState();
            item.getAnswer().setState(convertFromUiState(state));

            notifyStateChanged(item, previousState);
        }

        @Override
        public boolean onLongClick(View v) {
            for (SubcriteriaCallback subscriber : subscribers) {
                subscriber.onSubCriteriaCallForHint(itemView, getItem());
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textview_comment_button:
                    for (SubcriteriaCallback subscriber : subscribers) {
                        subscriber.onSubCriteriaCallForCommentEdit(getItem());
                    }
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

        private void notifyStateChanged(SubCriteria subCriteria, Answer.State previousState) {
            for (SubcriteriaCallback subscriber : subscribers) {
                subscriber.onSubCriteriaStateChanged(subCriteria, previousState);
            }
        }
    }

}
