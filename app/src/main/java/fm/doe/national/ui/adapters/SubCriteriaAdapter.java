package fm.doe.national.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.mock.MockSubCriteria;
import fm.doe.national.ui.custom_views.SwitchableButton;
import fm.doe.national.ui.listeners.SubcriteriaStateChangeListener;

public class SubCriteriaAdapter extends RecyclerView.Adapter<SubCriteriaAdapter.SubCriteriaViewHolder> {

    private List<MockSubCriteria> items;
    private SubcriteriaStateChangeListener listener;

    public SubCriteriaAdapter(@NonNull List<MockSubCriteria> subCriterias, @Nullable SubcriteriaStateChangeListener listener) {
        super();
        this.items = subCriterias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubCriteriaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_criteria, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubCriteriaViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
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

        protected void bind(MockSubCriteria subCriteria, int position) {
            questionTextView.setText(subCriteria.getQuestion());
            numberingTextView.setText(String.format(Locale.US, "%c.", 'a' + position));
            switchableButton.setState(convertModelToUiState(subCriteria.getState()));
            switchableButton.setListener((View view, SwitchableButton.State state) -> {
                subCriteria.setState(convertUiToModelState(state));
                if (listener != null) {
                    listener.onStateChanged();
                }
            });
        }

        private MockSubCriteria.State convertUiToModelState(SwitchableButton.State state) {
            if (state == SwitchableButton.State.POSITIVE) {
                return MockSubCriteria.State.POSITIVE;
            } else if (state == SwitchableButton.State.NEGATIVE) {
                return MockSubCriteria.State.NEGATIVE;
            } else {
                return MockSubCriteria.State.NOT_ANSWERED;
            }
        }

        private SwitchableButton.State convertModelToUiState(MockSubCriteria.State state) {
            if (state == MockSubCriteria.State.POSITIVE) {
                return SwitchableButton.State.POSITIVE;
            } else if (state == MockSubCriteria.State.NEGATIVE) {
                return SwitchableButton.State.NEGATIVE;
            } else {
                return SwitchableButton.State.NEUTRAL;
            }
        }
    }

}
