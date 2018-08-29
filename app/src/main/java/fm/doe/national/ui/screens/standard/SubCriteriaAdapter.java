package fm.doe.national.ui.screens.standard;

import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.ui.custom_views.SwitchableButton;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
import fm.doe.national.utils.TextUtil;

public class SubCriteriaAdapter extends BaseClickableAdapter<SubCriteriaViewData, SubCriteriaAdapter.SubCriteriaViewHolder> {

    private List<SubcriteriaStateChangeListener> subscribers = new ArrayList<>();

    public void clearSubscribers() {
        subscribers.clear();
    }

    public void unsubscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.remove(listener);
    }

    public void subscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.add(listener);
    }

    public void addSubscribers(List<SubcriteriaStateChangeListener> subscribers) {
        this.subscribers.addAll(subscribers);
    }

    @Override
    protected SubCriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new SubCriteriaViewHolder(parent);
    }

    protected class SubCriteriaViewHolder extends BaseRecyclerViewHolder<SubCriteriaViewData> implements SwitchableButton.StateChangedListener {

        @BindView(R.id.textview_alphabetical_numbering)
        TextView numberingTextView;

        @BindView(R.id.textview_question)
        TextView questionTextView;

        @BindView(R.id.switch_answer)
        SwitchableButton switchableButton;

        protected SubCriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sub_criteria);
            switchableButton.setListener(this);
        }

        @Override
        public void onBind() {
            String question = item.getText();
            questionTextView.setText(question.replace("\r\n", " ").replace("\n", " "));
            numberingTextView.setText(getResources().getString(
                    R.string.criteria_char_icon_pattern,
                    TextUtil.convertIntToCharsIcons(getAdapterPosition())));

            switchableButton.setState(convertToUiState(item.getAnswer()));
        }

        @Override
        public void onStateChanged(SwitchableButton view, SwitchableButton.State state) {
            item.setAnswer(convertFromUiState(state));
            notifyStateChanged(item);
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

        private void notifyStateChanged(SubCriteriaViewData subCriteria) {
            for (SubcriteriaStateChangeListener subscriber : subscribers) {
                subscriber.onSubCriteriaStateChanged(subCriteria, subCriteria.getCorrespondingSubCriteria(),
                        subCriteria.getCorrespondingAnswer(), subCriteria.getAnswer());
            }
        }
    }

}
