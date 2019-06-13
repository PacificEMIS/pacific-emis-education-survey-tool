package fm.doe.national.wash.ui.questions;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.wash.R;

public class ComplexNumericAnswerAdapter extends BaseListAdapter<String> {

    private SparseArray<String> answerStates = new SparseArray<>();
    private OnNumericAnswerChangeListener listener;

    public ComplexNumericAnswerAdapter(OnNumericAnswerChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    public void setAnswerStates(@NonNull SparseArray<String> answerStates) {
        this.answerStates = answerStates;
        notifyDataSetChanged();
    }

    @Nullable
    private String getAnswerStateAtPosition(int position) {
        return answerStates.get(position, null);
    }

    class ItemViewHolder extends ViewHolder implements TextWatcher {

        private TextView nameTextView = findViewById(R.id.textview);
        EditText editText = findViewById(R.id.textinputedittext);
        private ImageButton doneButton = findViewById(R.id.imagebutton_done);
        private String existingValue;

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_complex_numeric_answer);
            doneButton.setOnClickListener(this);
            editText.addTextChangedListener(this);
        }

        @Override
        protected void onBind(String item) {
            nameTextView.setText(item);
            existingValue = getAnswerStateAtPosition(getAdapterPosition());
            editText.setText(existingValue);
            doneButton.setVisibility(View.GONE);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            doneButton.setVisibility(editText.getText().toString().equals(existingValue) ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imagebutton_done) {
                String inputtedText = editText.getText().toString();
                existingValue = inputtedText.isEmpty() ? null : inputtedText;
                listener.onBinaryAnswerChange(getAdapterPosition(), existingValue);
                doneButton.setVisibility(View.GONE);
                hideKeyboard();
            } else {
                super.onClick(v);
            }
        }

        private void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            itemView.requestFocus();
        }

    }

    public interface OnNumericAnswerChangeListener {
        void onBinaryAnswerChange(int atPosition, @Nullable String input);
    }
}
