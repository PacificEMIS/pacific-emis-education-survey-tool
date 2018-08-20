package fm.doe.national.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.ui.custom_views.SwitchableButton;
import fm.doe.national.ui.listeners.SubcriteriaStateChangeListener;
import fm.doe.national.ui.view_data.SubCriteriaViewData;

public class SubCriteriaAdapter extends RecyclerView.Adapter<SubCriteriaAdapter.SubCriteriaViewHolder> {

    private List<SubCriteriaViewData> subCriterias;
    private List<SubcriteriaStateChangeListener> subscribers;

    public SubCriteriaAdapter(List<SubCriteriaViewData> subCriterias) {
        this.subCriterias = subCriterias;
        subscribers = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubCriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubCriteriaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_criteria, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubCriteriaViewHolder holder, int position) {
        holder.bind(subCriterias.get(position), position);
    }

    @Override
    public int getItemCount() {
        return subCriterias.size();
    }

    public void flushSubscribers() {
        subscribers.clear();
    }

    public void unsubscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.remove(listener);
    }

    public void subscribeOnChanges(SubcriteriaStateChangeListener listener) {
        subscribers.add(listener);
    }

    public void passSubscribers(List<SubcriteriaStateChangeListener> subscribers) {
        this.subscribers.addAll(subscribers);
    }

    protected class SubCriteriaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_alphabetical_numbering)
        TextView numberingTextView;

        @BindView(R.id.textview_question)
        TextView questionTextView;

        @BindView(R.id.switch_answer)
        SwitchableButton switchableButton;


        protected SubCriteriaViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        protected void bind(SubCriteriaViewData subCriteria, int position) {
            questionTextView.setText(subCriteria.getText());
            numberingTextView.setText(String.format(Locale.US, "%c.", 'a' + position));

            switchableButton.setState(convertToUiState(subCriteria.getAnswer()));
            switchableButton.setListener((View view, SwitchableButton.State state) -> {
                subCriteria.setAnswer(convertFromUiState(state));
                notifyStateChanged(subCriteria);
            });
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
            if (subscribers != null) {
                for (SubcriteriaStateChangeListener subscriber: subscribers) {
                    subscriber.onStateChanged(subCriteria, subCriteria.getCorrespondingSubCriteria(),
                            subCriteria.getCorrespondingAnswer(), subCriteria.getAnswer());
                }
            }
        }
    }

}
