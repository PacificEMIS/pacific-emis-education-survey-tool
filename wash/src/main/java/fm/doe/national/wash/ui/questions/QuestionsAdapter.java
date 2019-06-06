package fm.doe.national.wash.ui.questions;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.survey_core.ui.custom_views.BinaryAnswerSelectorView;
import fm.doe.national.wash.R;
import fm.doe.national.wash.ui.custom_views.answer_selector_view.AnswerSelectorView;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;
import fm.doe.national.wash_core.data.model.mutable.MutableAnswer;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;

public class QuestionsAdapter extends BaseListAdapter<MutableQuestion> {

    private QuestionsListener questionsListener;

    public QuestionsAdapter(QuestionsListener questionsListener) {
        this.questionsListener = questionsListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.ordinal() == viewType) {
                return provideViewHolderForQuestionType(questionType, parent);
            }
        }
        throw new IllegalStateException();
    }

    private ViewHolder provideViewHolderForQuestionType(QuestionType questionType, ViewGroup parent) {
        switch (questionType) {
            case BINARY:
                return new BinaryQuestionViewHolder(parent);
            case TERNARY:
                return new TernaryViewHolder(parent);
            case TEXT_INPUT:
                return new TextInputViewHolder(parent);
            case NUMBER_INPUT:
                return new NumericTextInputViewHolder(parent);
            case PHONE_INPUT:
                return new PhoneTextInputViewHolder(parent);
            case GEOLOCATION:
                break;
            case PHOTO:
                break;
            case SINGLE_SELECTION:
                return new SingleSelectionViewHolder(parent);
            case MULTI_SELECTION:
                return new MultipleSelectionViewHolder(parent);
            case COMPLEX_BINARY:
                break;
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        // unused
        return null;
    }

    class BinaryQuestionViewHolder extends QuestionViewHolder implements BinaryAnswerSelectorView.StateChangedListener {

        private BinaryAnswerSelectorView binaryAnswerSelectorView;

        BinaryQuestionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_binary_question);
            binaryAnswerSelectorView.setListener(this);
        }

        @Override
        protected void bindViews() {
            super.bindViews();
            binaryAnswerSelectorView = findViewById(R.id.binaryanswerselectorview);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);
            MutableAnswer answer = getItem().getAnswer();

            if (answer == null) {
                return;
            }

            binaryAnswerSelectorView.setStateNotNotifying(convertToUiState(answer.getBinaryAnswerState()));
        }

        @Override
        public void onStateChanged(BinaryAnswerSelectorView view, BinaryAnswerSelectorView.State state) {
            MutableAnswer answer = getItem().getAnswer();

            if (answer == null) {
                return;
            }

            answer.setBinaryAnswerState(convertFromUiState(state));
            questionsListener.onAnswerStateChanged(getItem());
        }

        private BinaryAnswerSelectorView.State convertToUiState(@Nullable BinaryAnswerState state) {
            if (state == null) {
                return BinaryAnswerSelectorView.State.NEUTRAL;
            }

            switch (state) {
                case YES:
                    return BinaryAnswerSelectorView.State.POSITIVE;
                case NO:
                    return BinaryAnswerSelectorView.State.NEGATIVE;
            }

            throw new IllegalStateException();
        }

        @Nullable
        private BinaryAnswerState convertFromUiState(BinaryAnswerSelectorView.State state) {
            switch (state) {
                case NEUTRAL:
                    return null;
                case NEGATIVE:
                    return BinaryAnswerState.NO;
                case POSITIVE:
                    return BinaryAnswerState.YES;
            }
            throw new IllegalStateException();
        }
    }

    class SingleSelectionViewHolder extends SelectionViewHolder {

        SingleSelectionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_single_selection_question);
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            MutableAnswer answer = question.getAnswer();
            List<String> items = question.getItems();
            List<Integer> selectedIndexes = findSelectedIndexes();
            Integer currentSelectedIndex = selectedIndexes.isEmpty() ? null : selectedIndexes.get(0);

            if (answer == null || CollectionUtils.isEmpty(items) || items.size() <= atPosition) {
                return;
            }

            if (checked) {
                answer.setItems(Collections.singletonList(items.get(atPosition)));
            } else if (currentSelectedIndex != null && currentSelectedIndex == atPosition) {
                answer.setItems(null);
            }

            questionsListener.onAnswerStateChanged(question);
        }
    }

    class MultipleSelectionViewHolder extends SelectionViewHolder {

        MultipleSelectionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_multiple_selection_question);
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            MutableAnswer answer = question.getAnswer();
            List<String> items = question.getItems();

            if (!CollectionUtils.isEmpty(items) && items.size() > atPosition && answer != null) {
                List<String> existingAnswerItems = answer.getItems();
                ArrayList<String> wrappedExistingAnswerItems =
                        existingAnswerItems == null ? null : new ArrayList<>(existingAnswerItems);
                String item = items.get(atPosition);

                if (checked) {
                    if (wrappedExistingAnswerItems == null) {
                        wrappedExistingAnswerItems = new ArrayList<>();
                    }

                    wrappedExistingAnswerItems.add(item);
                } else if (wrappedExistingAnswerItems != null) {
                    wrappedExistingAnswerItems.remove(item);

                    if (wrappedExistingAnswerItems.isEmpty()) {
                        wrappedExistingAnswerItems = null;
                    }
                }

                answer.setItems(wrappedExistingAnswerItems);
                questionsListener.onAnswerStateChanged(question);
            }
        }
    }

    class TernaryViewHolder extends QuestionViewHolder implements AnswerSelectorView.Listener {

        private AnswerSelectorView answerSelectorView = findViewById(R.id.answerselectionview);

        TernaryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_single_selection_question);
            answerSelectorView.setListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            List<String> values = Arrays.stream(TernaryAnswerState.values())
                    .map(v -> v.getText().getString(getContext()))
                    .collect(Collectors.toList());

            MutableAnswer answer = item.getAnswer();
            TernaryAnswerState answerState = answer != null ? answer.getTernaryAnswerState() : null;
            Integer selectedIndex = answerState != null ? answerState.ordinal() : null;

            answerSelectorView.setItems(
                    values,
                    selectedIndex != null ? CollectionUtils.singletonArrayList(selectedIndex) : CollectionUtils.emptyArrayList()
            );
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            MutableAnswer answer = question.getAnswer();

            if (answer == null) {
                return;
            }

            TernaryAnswerState answerState = answer.getTernaryAnswerState();
            Integer currentAnswerStatePosition = answerState != null ? answerState.ordinal() : null;

            if (checked) {
                answer.setTernaryAnswerState(TernaryAnswerState.values()[atPosition]);
            } else if (currentAnswerStatePosition != null && atPosition == currentAnswerStatePosition) {
                answer.setTernaryAnswerState(null);
            }

            questionsListener.onAnswerStateChanged(question);
        }

    }

    class NumericTextInputViewHolder extends TextInputViewHolder {

        public NumericTextInputViewHolder(ViewGroup parent) {
            super(parent);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }

    class PhoneTextInputViewHolder extends TextInputViewHolder {

        public PhoneTextInputViewHolder(ViewGroup parent) {
            super(parent);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }

    }

    class TextInputViewHolder extends QuestionViewHolder implements TextWatcher {

        EditText editText = findViewById(R.id.textinputedittext);
        private ImageButton doneButton = findViewById(R.id.imagebutton_done);
        private String existingValue;

        TextInputViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_text_input_question);
            doneButton.setOnClickListener(this);
            editText.addTextChangedListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            if (item.getAnswer() != null) {
                existingValue = item.getAnswer().getInputText();
                editText.setText(existingValue);
            }
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
                MutableQuestion question = getItem();
                MutableAnswer answer = question.getAnswer();

                if (answer != null) {
                    existingValue = inputtedText.isEmpty() ? null : inputtedText;
                    answer.setInputText(existingValue);
                    questionsListener.onAnswerStateChanged(question);
                    doneButton.setVisibility(View.GONE);
                }

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

    private abstract class SelectionViewHolder extends QuestionViewHolder implements AnswerSelectorView.Listener {

        private AnswerSelectorView answerSelectorView = findViewById(R.id.answerselectionview);

        SelectionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            answerSelectorView.setListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);
            answerSelectorView.setItems(item.getItems(), findSelectedIndexes());
        }

        @NonNull
        ArrayList<Integer> findSelectedIndexes() {
            MutableAnswer answer = getItem().getAnswer();

            if (CollectionUtils.isEmpty(getItem().getItems()) || answer == null || CollectionUtils.isEmpty(answer.getItems())) {
                return CollectionUtils.emptyArrayList();
            }

            List<String> items = getItem().getItems();
            ArrayList<Integer> selectedIndexes = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                if (answer.getItems().contains(items.get(i))) {
                    selectedIndexes.add(i);
                }
            }

            return selectedIndexes;
        }
    }

    private abstract class QuestionViewHolder extends ViewHolder {

        private TextView prefixTextView;
        private TextView titleTextView;
        private ImageButton photosButton;
        private ImageButton commentButton;

        QuestionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            bindViews();
            photosButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            titleTextView.setOnLongClickListener(this);
        }

        protected void bindViews() {
            prefixTextView = findViewById(R.id.textview_prefix);
            titleTextView = findViewById(R.id.textview_title);
            photosButton = findViewById(R.id.imagebutton_photo);
            commentButton = findViewById(R.id.imagebutton_comment);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            prefixTextView.setText(item.getPrefix());
            titleTextView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imagebutton_photo) {
                questionsListener.onPhotoPressed(getItem());
            } else if (id == R.id.imagebutton_comment) {
                questionsListener.onCommentPressed(getItem());
            } else {
                super.onClick(v);
            }
        }
    }

    public interface QuestionsListener {

        void onPhotoPressed(MutableQuestion question);

        void onCommentPressed(MutableQuestion question);

        void onAnswerStateChanged(MutableQuestion question);

    }
}
