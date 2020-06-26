package org.pacific_emis.surveys.wash.ui.questions;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import org.pacific_emis.surveys.wash.R;
import org.pacific_emis.surveys.wash_core.data.model.BinaryAnswerState;

public class ComplexBinaryAnswerAdapter extends BaseListAdapter<String> {

    private SparseArray<BinaryAnswerState> answerStates = new SparseArray<>();
    private OnBinaryAnswerChangeListener listener;

    public ComplexBinaryAnswerAdapter(OnBinaryAnswerChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    public void setAnswerStates(@NonNull SparseArray<BinaryAnswerState> answerStates) {
        this.answerStates = answerStates;
        notifyDataSetChanged();
    }

    @Nullable
    private BinaryAnswerState getAnswerStateAtPosition(int position) {
        return answerStates.get(position, null);
    }

    private void onPositivePressedAtPosition(int position) {
        BinaryAnswerState currentState = getAnswerStateAtPosition(position);
        if (currentState == BinaryAnswerState.YES) {
            setNeutralStateAtPosition(position);
        } else {
            setPositiveStateAtPosition(position);
        }
        notifyItemChanged(position);
    }

    private void onNegativePressedAtPosition(int position) {
        BinaryAnswerState currentState = getAnswerStateAtPosition(position);
        if (currentState == BinaryAnswerState.NO) {
            setNeutralStateAtPosition(position);
        } else {
            setNegativeStateAtPosition(position);
        }
        notifyItemChanged(position);
    }

    private void setPositiveStateAtPosition(int position) {
        setAnswerState(position, BinaryAnswerState.YES);
    }

    private void setNegativeStateAtPosition(int position) {
        setAnswerState(position, BinaryAnswerState.NO);
    }

    private void setAnswerState(int position, BinaryAnswerState state) {
        answerStates.put(position, state);
        listener.onBinaryAnswerChange(position, state);
    }

    private void setNeutralStateAtPosition(int position) {
        answerStates.delete(position);
        listener.onBinaryAnswerChange(position, null);
    }

    class ItemViewHolder extends ViewHolder {

        private TextView nameTextView = findViewById(R.id.textview);
        private View positiveView = findViewById(R.id.layout_positive);
        private View negativeView = findViewById(R.id.layout_negative);
        private RadioButton positiveRadioButton = findViewById(R.id.radiobutton_positive);
        private RadioButton negativeRadioButton = findViewById(R.id.radiobutton_negative);

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_complex_binary_answer);

            positiveView.setOnClickListener(this);
            negativeView.setOnClickListener(this);
        }

        @Override
        protected void onBind(String item) {
            nameTextView.setText(item);

            BinaryAnswerState state = getAnswerStateAtPosition(getAdapterPosition());

            boolean isPositive = state == BinaryAnswerState.YES;
            boolean isNegative = state == BinaryAnswerState.NO;
            positiveView.setActivated(isPositive);
            negativeView.setActivated(isNegative);
            positiveRadioButton.setChecked(isPositive);
            negativeRadioButton.setChecked(isNegative);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (v.getId() == R.id.layout_positive) {
                onPositivePressedAtPosition(pos);
            } else if (v.getId() == R.id.layout_negative) {
                onNegativePressedAtPosition(pos);
            } else {
                super.onClick(v);
            }
        }
    }

    public interface OnBinaryAnswerChangeListener {
        void onBinaryAnswerChange(int atPosition, @Nullable BinaryAnswerState binaryAnswerState);
    }
}
